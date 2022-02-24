#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int** get_sub_map(int **map, int key_size, int x, int y);
int** get_product(int **sub_map, int **key, int key_size);

void main(int argv, char *argc[]) {
	FILE *fp_map, *fp_key, *fp_out;										// File handlers for mapmatrix.txt, keymatrix.txt and output.txt files
	char *filename_map, *filename_key, *filename_out;
	char *map_size, *dummy;												// Example: map_size = "18x18"
	int key_size, center_indx_offset;									// Example: key_size = 3, center_indx_offset = 1,1 for key_size = 3 and 2,2 for key_size = 5, i.e., (key_size - 1)/2
	size_t len, x_indx;
	int map_dimen[] = { 0, 0 };											// Example: map_dimen = {18, 18}
	int **map, **key, **sub_map, **product, x, y;						// sub_map = part of the 'map' matrix under the 'key' matrix (2D dynamic arrays)

	size_t i, j;
	int var;

	map_size = (char*)malloc(sizeof(argc[1]));							// Dynamic allocation
	strcpy(map_size, argc[1]);
	len = strlen(map_size);

	// Finding the index of character 'x' in "18x18"
	for (i = 0; i < len; ++i) {
		if (map_size[i] == 'x') {
			x_indx = i;
			break;
		}
	}
	dummy = (char*)malloc(x_indx);
	strncpy(dummy, map_size, x_indx);									// Example: Copying "18" out of "18x18" to dummy
	map_dimen[0] = atoi(dummy);											// Example: map_dimen[0] = 18
	for (i = x_indx + 1; i < len; ++i)
		dummy[i - x_indx - 1] = map_size[i];
	map_dimen[1] = atoi(dummy);											// Example: map_dimen[1] = 18 (the second "18" in 18x18)

	key_size = atoi(argc[2]);
	center_indx_offset = (key_size - 1) / 2;

	filename_map = (char*)malloc(sizeof(argc[3])); strcpy(filename_map, argc[3]); fp_map = fopen(filename_map, "r");  // Allocating memory for the filename_* C strings
	filename_key = (char*)malloc(sizeof(argc[4])); strcpy(filename_key, argc[4]); fp_key = fopen(filename_key, "r");  // and opening the respective files
	filename_out = (char*)malloc(sizeof(argc[5])); strcpy(filename_out, argc[5]); fp_out = fopen(filename_out, "w");

	// Allocating memory for mapmatrix and keymatrix
	map = (int**)malloc(map_dimen[0] * sizeof(int*));
	for (i = 0; i < map_dimen[0]; ++i)
		map[i] = (int*)malloc(map_dimen[1] * sizeof(int));
	key = (int**)malloc(key_size * sizeof(int*));
	for (i = 0; i < key_size; ++i)
		key[i] = (int*)malloc(key_size * sizeof(int));

	// Read in from mapmatrix.txt file into map
	while (!feof(fp_map)) {
		for (i = 0; i < map_dimen[0]; ++i) {
			for (j = 0; j < map_dimen[1]; ++j) {
				fscanf(fp_map, "%d ", &var);							// Read in each integer to var
				map[i][j] = var;										// and then into the matrix (2D array), map
			}
		}
	}

	// Read in from keymatrix.txt file into key
	while (!feof(fp_key)) {
		for (i = 0; i < key_size; ++i) {
			for (j = 0; j < key_size; ++j) {
				fscanf(fp_key, "%d ", &var);							// Same as in the case of map
				key[i][j] = var;
			}
		}
	}

	// Just printing both the matrices to screen
	printf("Map Matrix\n");
	for (i = 0; i < map_dimen[0]; ++i) {
		for (j = 0; j < map_dimen[1]; ++j) {
			printf("%d ", map[i][j]);
		}
		printf("\n");
	}
	printf("\n");

	printf("Key Matrix\n");
	for (i = 0; i < key_size; ++i) {
		for (j = 0; j < key_size; ++j) {
			printf("%d ", key[i][j]);
		}
		printf("\n");
	}
	printf("\n");

	x = 0; y = 0;
	while (1) {															// Loop until treasure is found
		sub_map = get_sub_map(map, key_size, x, y);						// Get the sub_map
		printf("Sub Map\n");
		for (i = 0; i < key_size; ++i) {
			for (j = 0; j < key_size; ++j) {
				printf("%d ", sub_map[i][j]);
			}
			printf("\n");
		}
		printf("\n");

		product = get_product(sub_map, key, key_size);					// Calculate the product between sub_map and key matrix

		// Sum up the values in the product matrix and take up the modulus by 5
		int sum = 0;
		for (i = 0; i < key_size; ++i) {
			for (j = 0; j < key_size; ++j) {
				sum += product[i][j];
			}
		}
		int mod = sum % 5;

		/* Required conditions for (x,y) to lie within the map matrix, without jumping out
		   These are used in the following condition checks.
		   So, when moving left/right, need to check y values
		   and when moving up/down, need to check x values
		if (x >= 0 &&
			x <= (map_dimen[0] - key_size) &&
			y >= 0 &&
			y <= (map_dimen[1] - key_size)) {
		}
		*/

		if (mod == 0) {
			printf("Treasure Found.\n");
			fprintf(fp_out, "%d,%d:%d\n", x + center_indx_offset, y + center_indx_offset, sum);  // Printing out the results to output.txt file
																								 // + center_indx_offset is because the (x,y) starts with (0,0) and
																								 // we need (center_indx_offset, center_indx_offset) when (x,y) is (0,0)
			break;
		}
		else if (mod == 1) {
			int x_h = x;
			x = x - key_size;											// Try to move up
			if (x >= 0 &&
				x <= (map_dimen[0] - key_size)) {						// New x is within the map matrix (Succeeded in moving up)
				printf("============== Go Up ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x_h + center_indx_offset, y + center_indx_offset, sum);
				continue;
			}
			else {														// New x went outside the map matrix (Failed in moving up)
				// Can't move up. So go down.
				printf("============== Go Down ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x_h + center_indx_offset, y + center_indx_offset, sum);
				x = x_h + key_size;										// So, move down
			}
		}
		else if (mod == 2) {
			int x_h = x;
			x = x + key_size;											// Try to move down
			if (x >= 0 &&
				x <= (map_dimen[0] - key_size)) {						// New x is within the map matrix (Succeeded in moving down)
				printf("============== Go Down ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x_h + center_indx_offset, y + center_indx_offset, sum);
				continue;
			}
			else {														// New x went outside the map matrix (Failed in moving down)
				// Can't move down. So go up.
				printf("============== Go Up ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x_h + center_indx_offset, y + center_indx_offset, sum);
				x = x_h - key_size;										// So, move up
			}
		}
		else if (mod == 3) {
			int y_h = y;
			y = y + key_size;											// Try to move right
			if (y >= 0 &&
				y <= (map_dimen[1] - key_size)) {						// New y is within the map matrix (Succeeded in moving right)
				printf("============== Go Right ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x + center_indx_offset, y_h + center_indx_offset, sum);
				continue;
			}
			else {														// New y went outside the map matrix (Failed in moving right)
				// Can't move right. So go left.
				printf("============== Go Left ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x + center_indx_offset, y_h + center_indx_offset, sum);
				y = y_h - key_size;										// So, move left
			}
		}
		else if (mod == 4) {
			int y_h = y;
			y = y - key_size;											// Try to move left
			if (y >= 0 &&
				y <= (map_dimen[1] - key_size)) {						// New y is within the map matrix (Succeeded in moving left)
				printf("============== Go Left ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x + center_indx_offset, y_h + center_indx_offset, sum);
				continue;
			}
			else {														// New y went outside the map matrix (Failed in moving left)
				// Can't move left. So go right.
				printf("============== Go Right ==============\n");
				fprintf(fp_out, "%d,%d:%d\n", x + center_indx_offset, y_h + center_indx_offset, sum);
				y = y_h + key_size;										// So, move right
			}
		}
	}

	fclose(fp_map); fclose(fp_key); fclose(fp_out);						// Close all files
}

int** get_sub_map(int **map, int key_size, int x, int y) {
	int i, j;
	// Allocating memory for the sub_map matrix
	int **sub_map = (int**)malloc(key_size * sizeof(int*));
	for (i = 0; i < key_size; ++i) {
		sub_map[i] = (int*)malloc(key_size * sizeof(int));
	}
	// Copy "key_size X key_size" portion of map into sub_map starting at (x,y)
	for (i = x; i < x + key_size; ++i) {
		for (j = y; j < y + key_size; ++j) {
			sub_map[i - x][j - y] = map[i][j];
		}
	}
	return sub_map;
}

int** get_product(int **sub_map, int **key, int key_size) {
	int i, j;
	// Allocating memory for product matrix
	int **product = (int**)malloc(key_size * sizeof(int*));
	for (i = 0; i < key_size; ++i) {
		product[i] = (int*)malloc(key_size * sizeof(int));
	}
	// Element-wise product of sub_map and key matrices
	for (i = 0; i < key_size; ++i) {
		for (j = 0; j < key_size; ++j) {
			product[i][j] = sub_map[i][j] * key[i][j];
		}
	}
	return product;
}
