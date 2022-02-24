#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <malloc.h>
#include <time.h>

// Structure of the frame
struct frame {
	int top;
	char sender_id, receiver_id;					// IDs
	int sender_port, receiver_port;				// Ports
	int sender_ip[4], receiver_ip[4];			// IPs
	char *sender_mac, *receiver_mac;			// MACs
	char *msg;														// Message in the frame
};

// push() for layer 1 (IDs and Message)
void push_l1(struct frame *f, char s_id, char r_id, char *msg, int *top) {
	if (*top == -1) {
		f->sender_id = s_id; f->receiver_id = r_id;
		f->msg = (char*)malloc(strlen(msg) * sizeof(char));
		strcpy(f->msg, msg);
		(*top)++;
	}
}

// push() for layer 2 (Ports)
void push_l2(struct frame *f, int s_port, int r_port, int *top) {
	if (*top == 0) {
		f->sender_port = s_port; f->receiver_port = r_port;
		(*top)++;
	}
}

// push() for layer 3 (IPs)
void push_l3(struct frame *f, int *s_ip, int *r_ip, int *top) {
	if (*top == 1) {
		int i = 0;
		for (i = 0; i < 4; ++i) {
			f->sender_ip[i] = s_ip[i];
			f->receiver_ip[i] = r_ip[i];
		}
		(*top)++;
	}
}

// push() for layer 4 (MACs)
void push_l4(struct frame *f, char *s_mac, char *r_mac, int *top) {
	if (*top == 2) {
		f->sender_mac = (char*)malloc(strlen(s_mac) * sizeof(char));
		f->receiver_mac = (char*)malloc(strlen(r_mac) * sizeof(char));
		strcpy(f->sender_mac, s_mac);
		strcpy(f->receiver_mac, r_mac);
		(*top)++;
	}
}

// pop() for layer 1 (IDs and Message)
void pop_l1(struct frame *f, char *s_id, char *r_id, char **msg, int *top) {
	if (*top == 0) {
		*s_id = f->sender_id; *r_id = f->receiver_id;
		*msg = (char*)malloc(strlen(f->msg) * sizeof(char));
		strcpy(*msg, f->msg);
		(*top)--;
	}
}

// pop() for layer 2 (Ports)
void pop_l2(struct frame *f, int *s_port, int *r_port, int *top) {
	if (*top == 1) {
		*s_port = f->sender_port; *r_port = f->receiver_port;
		(*top)--;
	}
}

// pop() for layer 3 (IPs)
void pop_l3(struct frame *f, int **s_ip, int **r_ip, int *top) {
	if (*top == 2) {
		int i = 0;
		*s_ip = (int*)malloc(4 * sizeof(int));
		*r_ip = (int*)malloc(4 * sizeof(int));
		for (i = 0; i < 4; ++i) {
			(*s_ip)[i] = f->sender_ip[i];
			(*r_ip)[i] = f->receiver_ip[i];
		}
		(*top)--;
	}
}

// pop() for layer 4 (MACs)
void pop_l4(struct frame *f, char **s_mac, char **r_mac, int *top) {
	if (*top == 3) {
		*s_mac = (char*)malloc(strlen(f->sender_mac) * sizeof(char));
		*r_mac = (char*)malloc(strlen(f->receiver_mac) * sizeof(char));
		strcpy(*s_mac, f->sender_mac);
		strcpy(*r_mac, f->receiver_mac);
		(*top)--;
	}
}

// Structure for queue
struct queue {
	char client_id;
	int size;						// Number of frames in the queue
	struct frame *f;		// Array of frames in the queue
};

// Queue insert (at the end) function
void q_insert(struct queue *q, struct frame f) {
	int i = 0;
	struct frame *ff = (struct frame*)malloc((q->size + 1) * sizeof(struct frame));
	for (i = 0; i < q->size; ++i) {
		ff[i] = q->f[i];
	}
	ff[i] = f;
	if (q->size > 0) {
		free(q->f);
	}
	q->f = ff;
	q->size++;
}

// Queue remove (from beginning) function
struct frame q_remove(struct queue *q) {
	int i = 0;
	struct frame *ff = (struct frame*)malloc((q->size - 1) * sizeof(struct frame));
	for (i = 1; i < q->size; ++i) {
		ff[i - 1] = q->f[i];
	}
	struct frame ret_f = q->f[0];
	q->f = ff;
	q->size--;
	return ret_f;
}

// Structure for individual log message
struct log {
	char *timestamp;
	char *message;
	int frame_count;
	int num_hops;
	char s_id, r_id;
	char *activity;
	char *success;
};

// Function for adding a log entry for a client
void add_log_entry(struct log **client_entries, int *num_log_entries, time_t timestamp, char *message, int frame_count, int hop, char s_id, char r_id, char *activity, char *success) {
	if (*num_log_entries == 0) {
		// New log
		*client_entries = (struct log*)malloc(sizeof(struct log));
		char *ctime_string = ctime(&timestamp);
		(*client_entries)->timestamp = (char*)malloc(strlen(ctime_string) * sizeof(char));
		strcpy((*client_entries)->timestamp, ctime(&timestamp));
		(*client_entries)->message = (char*)malloc(strlen(message) * sizeof(char));
		strcpy((*client_entries)->message, message);
		(*client_entries)->frame_count = frame_count;
		(*client_entries)->num_hops = hop;
		(*client_entries)->s_id = s_id;
		(*client_entries)->r_id = r_id;
		(*client_entries)->activity = (char*)malloc(strlen(activity) * sizeof(char));
		strcpy((*client_entries)->activity, activity);
		(*client_entries)->success = (char*)malloc(strlen(success) * sizeof(char));
		strcpy((*client_entries)->success, success);
	}
	else {
		// Append to existing logs
		*client_entries = (struct log*)realloc(*client_entries, (*num_log_entries + 1)*sizeof(struct log));
		char *ctime_string = ctime(&timestamp);
		(*client_entries)[*num_log_entries].timestamp = (char*)malloc(strlen(ctime_string) * sizeof(char));
		strcpy((*client_entries)[*num_log_entries].timestamp, ctime(&timestamp));
		(*client_entries)[*num_log_entries].message = (char*)malloc(strlen(message) * sizeof(char));
		strcpy((*client_entries)[*num_log_entries].message, message);
		(*client_entries)[*num_log_entries].frame_count = frame_count;
		(*client_entries)[*num_log_entries].num_hops = hop;
		(*client_entries)[*num_log_entries].s_id = s_id;
		(*client_entries)[*num_log_entries].r_id = r_id;
		(*client_entries)[*num_log_entries].activity = (char*)malloc(strlen(activity) * sizeof(char));
		strcpy((*client_entries)[*num_log_entries].activity, activity);
		(*client_entries)[*num_log_entries].success = (char*)malloc(strlen(success) * sizeof(char));
		strcpy((*client_entries)[*num_log_entries].success, success);
	}
	*num_log_entries += 1;
}

void main(int argv, char *argc[]) {
	FILE *fp_clients, *fp_routing, *fp_commands, *fp_out;
	char *filename_clients, *filename_routing, *filename_commands, *filename_out;								// Filenames for the input and output files
	int max_msg_size, in_port, out_port, num_clients, num_receivers, num_cmds, num_diff_cmds;		// Client & message related variables
	char *client_ids, **client_macs; int **client_ips;																					// Client related data
	char ***routing;																																						// Routing table
	int *cmds;																																									// All the commands
	char msg_sender, msg_receiver, *msg;																												// For MESSAGE command
	char client_f, *queue_f; int frame;																													// For SHOW_FRAME_INFO command
	char client_q, *queue_q;																																		// For SHOW_Q_INFO command
	char client_s;																																							// For SEND command
	char client_p;																																							// For PRINT_LOG command
	int hop = 0;
	struct log **all_log_entries;																																// All log entries for all clients
	int *num_log_entries;

	int i, j, k, l, m, n, p;

	filename_clients = (char*)malloc(sizeof(argc[1]));																					// Read in the filename
	strcpy(filename_clients, argc[1]);																													// and
	fp_clients = fopen(filename_clients, "r");																									// open it

	filename_routing = (char*)malloc(sizeof(argc[2]));
	strcpy(filename_routing, argc[2]);
	fp_routing = fopen(filename_routing, "r");

	filename_commands = (char*)malloc(sizeof(argc[3]));
	strcpy(filename_commands, argc[3]);
	fp_commands = fopen(filename_commands, "r");

	filename_out = (char*)malloc(sizeof(argc[4]));
	strcpy(filename_out, argc[4]);
	fp_out = fopen(filename_out, "r");

	fscanf(fp_clients, "%d\n", &num_clients);
	client_ids = (char*)malloc(num_clients * sizeof(char));																			// Allocate the memory for clients IDs,
	client_ips = (int**)malloc(num_clients * sizeof(int*));																			// IPs and
	client_macs = (char**)malloc(num_clients * sizeof(char*));																	// MACs

	all_log_entries = (struct log**)malloc(num_clients * sizeof(struct log*));									// Allocate memory for log entries
	num_log_entries = (int*)malloc(num_clients * sizeof(int));
	for (i = 0; i < num_clients; ++i) {
		num_log_entries[i] = 0;
	}

	i = 0;
	// Read in the client related data
	while (!feof(fp_clients)) {
		fscanf(fp_clients, "%c ", &client_ids[i]);

		client_ips[i] = (int*)malloc(4 * sizeof(int));
		fscanf(fp_clients, "%d.%d.%d.%d ", &client_ips[i][0], &client_ips[i][1], &client_ips[i][2], &client_ips[i][3]);

		char *tmp_mac = (char*)malloc(100 * sizeof(char));

		fscanf(fp_clients, "%s ", tmp_mac);
		client_macs[i] = (char*)malloc(strlen(tmp_mac) * sizeof(char));
		strcpy(client_macs[i++], tmp_mac);
	}

	/*for (i = 0; i < num_clients; ++i) {
		printf("%c %d.%d.%d.%d %s\n", client_ids[i], client_ips[i][0], client_ips[i][1], client_ips[i][2], client_ips[i][3], client_macs[i]);
	}*/

	num_receivers = num_clients - 1;
	routing = (char***)malloc(num_clients * sizeof(char**));
	for (i = 0; i < num_clients; ++i) {
		routing[i] = (char**)malloc(num_receivers * sizeof(char*));
		for (j = 0; j < num_receivers; ++j) {
			routing[i][j] = (char*)malloc(2 * sizeof(char));
		}
	}

	i = 0; j = 0;
	// Read in the routing table
	while (!feof(fp_routing)) {
		char tmp_ch;
		fscanf(fp_routing, "%c ", &tmp_ch);
		if (tmp_ch != '-') {
			routing[i][j][0] = tmp_ch;
			fscanf(fp_routing, "%c ", &routing[i][j][1]);
			++j;
		}
		else {
			j = 0; ++i;
		}
	}

	/*for (i = 0; i < num_clients; ++i) {
		for (j = 0; j < num_receivers; ++j) {
			printf("%c %c\n", routing[i][j][0], routing[i][j][1]);
		}
	}*/

	max_msg_size = atoi(argc[4]);
	out_port = atoi(argc[5]);
	in_port = atoi(argc[6]);

	// Initialize the queues for the clients
	struct queue *all_out_queues = (struct queue *)malloc(num_clients * sizeof(struct queue));
	struct queue *all_in_queues = (struct queue *)malloc(num_clients * sizeof(struct queue));
	for (i = 0; i < num_clients; ++i) {
		all_out_queues[i].client_id = client_ids[i];
		all_in_queues[i].client_id = client_ids[i];
		all_out_queues[i].size = 0;
		all_in_queues[i].size = 0;
		all_out_queues[i].f = NULL;
		all_in_queues[i].f = NULL;
	}

	fscanf(fp_commands, "%d\n", &num_cmds);
	num_diff_cmds = 5;
	cmds = (int*)malloc(num_diff_cmds * sizeof(int));
	for (i = 0; i < num_diff_cmds; ++i)
		cmds[i] = 0;
	i = 0;
	// Read in each command and process it
	while (!feof(fp_commands)) {
		char *tmp_ch = (char*)malloc(100 * sizeof(char));
		fscanf(fp_commands, "%s ", tmp_ch);
		if (!strcmp(tmp_ch, "MESSAGE")) {
			// For command "MESSAGE"
			for (j = 0; j < num_diff_cmds; ++j)
				cmds[j] = 0;
			cmds[0] = 1;
			fscanf(fp_commands, "%c %c ", &msg_sender, &msg_receiver);
			// Read msg
			char *tmp_ch = (char*)malloc(1000 * sizeof(char));
			fscanf(fp_commands, "#%[^#]%*c#", tmp_ch);
			msg = (char*)malloc((1 + strlen(tmp_ch)) * sizeof(char));
			strcpy(msg, tmp_ch);
			int len = strlen("Command: MESSAGE") + 8 + strlen(msg);
			for (j = 0; j < len; ++j) printf("-");
			printf("\nCommand: MESSAGE %c %c #%s#\n", msg_sender, msg_receiver, msg);
			for (j = 0; j < len; ++j) printf("-");
			printf("\n");
			printf("Message to be sent: %s\n\n", msg);
			int msg_len = strlen(msg);
			int num_parts = msg_len / max_msg_size;
			if (msg_len % max_msg_size > 0) {
				num_parts++;
			}
			struct frame *frames = (struct frame*)malloc(num_parts * sizeof(struct frame));
			// Divide the big message into max_msg_size sized sub messages and load them into frames
			for (j = 0; j < num_parts; ++j) {
				printf("Frame #%d\n", j + 1);
				char *sub_msg = (char*)malloc(max_msg_size * sizeof(char));
				for (k = j * max_msg_size; k < (j + 1)*max_msg_size; ++k) {
					sub_msg[k - j * max_msg_size] = msg[k];
				}
				if (strlen(sub_msg) > max_msg_size) {
					for (k = max_msg_size; k < strlen(sub_msg); ++k) {
						sub_msg[k] = '\0';
					}
				}
				frames[j].top = -1;
				push_l1(&frames[j], msg_sender, msg_receiver, sub_msg, &frames[j].top);			// Layer 1
				push_l2(&frames[j], out_port, in_port, &frames[j].top);											// Layer 2

				for (k = 0; k < num_clients; ++k) {
					if (client_ids[k] == msg_sender)
						break;
				}
				for (l = 0; l < num_clients; ++l) {
					if (client_ids[l] == msg_receiver)
						break;
				}
				push_l3(&frames[j], client_ips[k], client_ips[l], &frames[j].top);					// Layer 3

				// Get the effective receiver_mac from routing table
				for (m = 0; m < num_receivers; ++m) {
					if (routing[k][m][0] == msg_receiver) {
						if (routing[k][m][1] == '-') {
							printf("Error: Unreachable destination. Packets are dropped after %d hops!\n", hop);
							break;
						}
						for (n = 0; n < num_clients; ++n) {
							if (routing[k][m][1] == client_ids[n]) {
								break;
							}
						}
						push_l4(&frames[j], client_macs[k], client_macs[n], &frames[j].top);  	// Layer 4
						break;
					}
				}

				printf("Sender MAC address: %s, Receiver MAC address: %s\n", frames[j].sender_mac, frames[j].receiver_mac);
				printf("Sender IP address: ");
				for (k = 0; k < 4; ++k) {
					printf("%d", frames[j].sender_ip[k]);
					if (k<3){
						printf(".");
					}
				}
				printf(", Receiver IP address: ");
				for (k = 0; k < 4; ++k) {
					printf("%d", frames[j].receiver_ip[k]);
					if (k<3){
						printf(".");
					}
				}
				printf("\n");
				printf("Sender port number: %d, Receiver port number: %d\n", frames[j].sender_port, frames[j].receiver_port);
				printf("Sender ID: %c, Receiver ID: %c\n", frames[j].sender_id, frames[j].receiver_id);
				printf("Message chunk carried: %s\n", frames[j].msg);
				int dash_len = strlen("Frame #") + 1;
				for (k = 0; k < dash_len; ++k) {
					printf("-");
				}
				printf("\n");

				// Insert frames into output queue of sender
				for (k = 0; k < num_clients; ++k) {
					if (client_ids[k] == msg_sender)
						break;
				}
				q_insert(&all_out_queues[k], frames[j]);																		// Insert the frames into the output queue for sender
			}
		}
		else {
			if (!strcmp(tmp_ch, "SHOW_FRAME_INFO")) {
				// For command "SHOW_FRAME_INFO"
				int len = strlen("Command: SHOW_FRAME_INFO") + 9;
				for (j = 0; j < len; ++j) printf("-");
				for (j = 0; j < num_diff_cmds; ++j)
					cmds[j] = 0;
				cmds[1] = 1;
				fscanf(fp_commands, "%c ", &client_f);
				// Read queue_f
				char *tmp_ch = (char*)malloc(10 * sizeof(char));
				fscanf(fp_commands, "%s", tmp_ch);
				queue_f = (char*)malloc((1 + strlen(tmp_ch)) * sizeof(char));
				strcpy(queue_f, tmp_ch);
				fscanf(fp_commands, "%d", &frame);
				printf("\nCommand: SHOW_FRAME_INFO %c %s %d\n", client_f, queue_f, frame);
				for (j = 0; j < len; ++j) printf("-");
				printf("\n");
				for (k = 0; k < num_clients; ++k) {
					if (client_ids[k] == client_f)
						break;
				}
				struct frame curr_f;
				int frame_not_found = 0;
				// For input/output queue, get the current frame
				if (!strcmp(queue_f, "in")) {
					if (frame <= all_in_queues[k].size) {
						curr_f = all_in_queues[k].f[frame - 1];
					}
					else {
						frame_not_found = 1;
						printf("Current frame #%d not found on the incoming queue of client %c\n", frame, client_f);
					}
				}
				else {
					if (frame <= all_out_queues[k].size) {
						curr_f = all_out_queues[k].f[frame - 1];
					}
					else {
						frame_not_found = 1;
						printf("Current frame #%d not found on the outgoing queue of client %c\n", frame, client_f);
					}
				}
				// If the current frame is not found, then print corresponding messages
				if (frame_not_found == 0) {
					printf("Current Frame #%d on the incoming queue of client %c\n", frame, client_f);
					printf("Carried Message: \"%s\"\n", curr_f.msg);
					printf("Layer 0 info: Sender ID: %c, Receiver ID: %c\n", curr_f.sender_id, curr_f.receiver_id);
					printf("Layer 1 info: Sender port number: %d, Receiver port number: %d\n", curr_f.sender_port, curr_f.receiver_port);
					printf("Layer 2 info: Sender IP address: ");
					for (l = 0; l < 4; ++l) {
						printf("%d", curr_f.sender_ip[l]);
						if(l<3){
							printf(".");
						}
					}
					printf(", Receiver IP address : ");
					for (l = 0; l < 4; ++l) {
						printf("%d", curr_f.receiver_ip[l]);
						if(l<3){
							printf(".");
						}
					}
					printf("\n");
					printf("Layer 3 info: Sender MAC address: %s, Receiver MAC address: %s\n", curr_f.sender_mac, curr_f.receiver_mac);
					printf("Number of hops so far: %d\n", hop);
				}
			}
			else {
				if (!strcmp(tmp_ch, "SHOW_Q_INFO")) {
					// For command "SHOW_Q_INFO"
					int len = strlen("Command: SHOW_Q_INFO") + 7;
					for (j = 0; j < len; ++j) printf("-");
					for (j = 0; j < num_diff_cmds; ++j)
						cmds[j] = 0;
					cmds[2] = 1;
					fscanf(fp_commands, "%c", &client_q);
					// Read queue_q
					char *tmp_ch = (char*)malloc(10 * sizeof(char));
					fscanf(fp_commands, "%s", tmp_ch);
					queue_q = (char*)malloc((1 + strlen(tmp_ch)) * sizeof(char));
					strcpy(queue_q, tmp_ch);
					printf("\nCommand: SHOW_Q_INFO %c %s\n", client_q, queue_q);
					for (j = 0; j < len; ++j) printf("-");
					printf("\n");
					for (k = 0; k < num_clients; ++k) {
						if (client_ids[k] == client_q)
							break;
					}
					// Display the input/output queue info
					if (!strcmp(queue_q, "in")) {
						printf("Client %c Incoming Queue Status\n", client_q);
						printf("Current total number of frames: %d\n", all_in_queues[k].size);
					}
					else {
						printf("Client %c Outgoing Queue Status\n", client_q);
						printf("Current total number of frames: %d\n", all_out_queues[k].size);
					}
				}
				else {
					if (!strcmp(tmp_ch, "SEND")) {
						// For command "SEND"
						int len = strlen("Command: SEND") + 3;
						for (j = 0; j < len; ++j) printf("-");
						for (j = 0; j < num_diff_cmds; ++j)
							cmds[j] = 0;
						cmds[3] = 1;
						fscanf(fp_commands, "%c", &client_s);
						printf("\nCommand: SEND %c\n", client_s);
						for (j = 0; j < len; ++j) printf("-");
						printf("\n");
						for (k = 0; k < num_clients; ++k) {
							if (client_ids[k] == client_s)
								break;
						}
						int k_copy = k, lost = 0;;
						struct frame curr_f;
						while (1) {
							// Until the message reaches the destination or lost.
							int frame_count = 0;
							while (all_out_queues[k].size > 0) {
								// Until the output queue becomes empty
								frame_count++;
								curr_f = q_remove(&all_out_queues[k]);
								for (l = 0; l < num_clients; ++l) {
									if (!strcmp(curr_f.receiver_mac, client_macs[l]))
										break;
								}

								for (m = 0; m < num_clients; ++m) {
									if (curr_f.receiver_id == client_ids[m])
										break;
								}

								if (l != m && frame_count == 1) {
									printf("A message received by client %c, but intended for client %c. Forwarding...\n", client_ids[l], client_ids[m]);
								}
								if (l != m) {
									strcpy(curr_f.sender_mac, client_macs[l]);
									for (n = 0; n < num_receivers; ++n) {
										if (routing[l][n][0] == msg_receiver) {
											if (routing[l][n][1] == '-') {
												// Lost
												lost = 1;
												break;
											}
											for (p = 0; p < num_clients; ++p) {
												if (routing[l][n][1] == client_ids[p]) {
													strcpy(curr_f.receiver_mac, client_macs[p]);
													break;
												}
											}
											break;
										}
									}
									if (!lost) {
										printf("\tFrame #%d MAC address change: New sender MAC %s, New receiver MAC %s\n", frame_count, curr_f.sender_mac, curr_f.receiver_mac);
									}
								}
								q_insert(&all_in_queues[l], curr_f);// Insert the frames into the incoming queue of the receiver
								if (l != m) {
									// If destination not same as the receiver,
									// remove from incoming queue of receiver and put it in the
									// outgoing queue of the receiver (for forwarding)
									q_insert(&all_out_queues[l], q_remove(&all_in_queues[l]));
								}
							}
							hop++;

							// Now the message (in parts) is in the input queue
							char *message = NULL;
							int count = 0;
							// Just getting the whole message from the received frames
							for (n = 0; n < all_out_queues[l].size; ++n) {
								struct frame c_f = all_out_queues[l].f[n];
								char *s_mac = NULL, *r_mac = NULL;
								int s_port = 0, r_port = 0, *s_ip = NULL, *r_ip = NULL;
								char s_id, r_id, *sub_msg = NULL;
								pop_l4(&c_f, &s_mac, &r_mac, &c_f.top);
								pop_l3(&c_f, &s_ip, &r_ip, &c_f.top);
								pop_l2(&c_f, &s_port, &r_port, &c_f.top);
								pop_l1(&c_f, &s_id, &r_id, &sub_msg, &c_f.top);
								if (count == 0) {
									message = (char*)malloc(strlen(sub_msg) * sizeof(char));
									strcpy(message, sub_msg);
								}
								else {
									strcat(message, sub_msg);
								}
								count++;
							}
							if (lost) {
								// When packet is lost
								printf("Error: Unreachable destination. Packets are dropped after %d hops!\n", hop);
								add_log_entry(&all_log_entries[l], &num_log_entries[l], time(NULL), message, frame_count, hop, client_ids[k_copy], client_ids[m], "Message Received", "Yes");
								add_log_entry(&all_log_entries[l], &num_log_entries[l], time(NULL), message, frame_count, hop, client_ids[k_copy], client_ids[m], "Message Forwarded", "No");
								break;
							}
							if (all_in_queues[l].f[0].receiver_id == client_ids[l]) {
								// MAC and ID matches - Frame reached the destination
								printf("A message received by client %c from client %c after a total of %d hops.\n", client_ids[l], client_ids[k_copy], hop);
								printf("Message: ");
								while(all_in_queues[l].size>0){
									struct frame c_f = q_remove(&all_in_queues[l]);
									char *s_mac = NULL, *r_mac = NULL;
									int s_port = 0, r_port = 0, *s_ip = NULL, *r_ip = NULL;
									char s_id, r_id, *sub_msg = NULL;
									pop_l4(&c_f, &s_mac, &r_mac, &c_f.top);
									pop_l3(&c_f, &s_ip, &r_ip, &c_f.top);
									pop_l2(&c_f, &s_port, &r_port, &c_f.top);
									pop_l1(&c_f, &s_id, &r_id, &sub_msg, &c_f.top);
									printf("%s", sub_msg);
								}
								printf("\n");
								break;
							}
							else {
								// Forward the frame and add to the log entries array
								add_log_entry(&all_log_entries[l], &num_log_entries[l], time(NULL), message, frame_count, hop, client_ids[k_copy], client_ids[m], "Message Received", "Yes");
								add_log_entry(&all_log_entries[l], &num_log_entries[l], time(NULL), message, frame_count, hop, client_ids[k_copy], client_ids[m], "Message Forwarded", "Yes");
								k = l;		// The receiver becomes the sender now and continue with the while(1) loop
							}
						}
					}
					else {
						if (!strcmp(tmp_ch, "PRINT_LOG")) {
							// For command "PRINT_LOG"
							int len = strlen("Command: PRINT_LOG") + 3;
							for (j = 0; j < len; ++j) printf("-");
							for (j = 0; j < num_diff_cmds; ++j)
								cmds[j] = 0;
							cmds[4] = 1;
							fscanf(fp_commands, "%c", &client_p);
							printf("\nCommand: PRINT_LOG %c\n", client_p);
							for (j = 0; j < len; ++j) printf("-");
							printf("\n");
							len = strlen("Client _ Logs:");
							printf("Client %c Logs:\n", client_p);
							for (j = 0; j < len; ++j) printf("-");
							printf("\n");

							for (k = 0; k < num_clients; ++k) {
								if (client_p == client_ids[k])
									break;
							}
							// Just display the log entries
							for(j=0;j<num_log_entries[k];++j){
								printf("Log Entry #%d:\n", j+1);
								printf("Timestamp: %s", all_log_entries[k][j].timestamp);
								printf("Message: %s\n", all_log_entries[k][j].message);
								printf("Number of frames: %d\n", all_log_entries[k][j].frame_count);
								printf("Number of hops: %d\n", all_log_entries[k][j].num_hops);
								printf("Sender ID: %c\n", all_log_entries[k][j].s_id);
								printf("Receiver ID: %c\n", all_log_entries[k][j].r_id);
								printf("Activity: %s\n", all_log_entries[k][j].activity);
								printf("Success: %s\n", all_log_entries[k][j].success);
								for (l = 0; l < len; ++l) printf("-");
								printf("\n");
							}
						}
						else {
							// For any other invalid command
							if (!strcmp(tmp_ch, " ")) continue;
							for (j = 0; j < num_diff_cmds; ++j)
								cmds[j] = 0;
							char *tmp_chh = (char*)malloc(1000 * sizeof(char));
							fscanf(fp_commands, "%[^\n]\n", tmp_chh);
							int len = strlen("Command: ") + strlen(tmp_ch) + strlen(tmp_chh) + 2;
							for (i = 0; i < len; ++i) printf("-");
							printf("\nCommand: %s %s\n", tmp_ch, tmp_chh);
							for (i = 0; i < len; ++i) printf("-");
							printf("\n");
							printf("Invalid command.\n");
						}
					}
				}
			}
		}
		++i;
	}
}
