package com.example.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class CarRentalSystemApplication {
    public static void main(String[] args) {
        int[] array1 = new int[]{1, 3, 7};
        int[] array2 = new int[]{2, 5, 9};
        int[] array3 = mergeSortedArrays(array1, array2);
        System.out.println("Merged Sorted Array is : " + Arrays.toString(array3));
        SpringApplication.run(CarRentalSystemApplication.class, args);
    }

    public static int[] mergeSortedArrays(int[] arr1, int[] arr2) {

        int arr1Length = arr1.length;
        int arr2Length = arr2.length;
        int[] result = new int[arr1Length + arr2Length];

        int i = 0, j = 0, k = 0;

        // I am comparing here the current smallest available elements
        while (i < arr1Length && j < arr2Length) {
            System.out.println("Comparing " + arr1[i] + " and " + arr2[j]);

            if (arr1[i] <= arr2[j]) {
                result[k++] = arr1[i++];
            } else {
                result[k++] = arr2[j++];
            }
        }

        // Copy any remaining elements from arr1
        while (i < arr1Length) {
            result[k++] = arr1[i++];
        }

        // Copy any remaining elements from arr2
        while (j < arr2Length) {
            result[k++] = arr2[j++];
        }

        return result;
    }

}


