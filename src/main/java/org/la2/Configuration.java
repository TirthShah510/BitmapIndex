package org.la2;

import java.io.File;

public interface Configuration {

    String FILE_PATH = ".." + File.separator + "Dataset/";
    String INPUT_FILE_NAME = "dataset.txt";
    String COMPRESSED_DATASET_FILE_NAME = "compressed_" + Configuration.INPUT_FILE_NAME;
    String BLOCK_NAME = "Block";

    String EMPLOYEE_ID_DIRECTORY = FILE_PATH + File.separator + "EmployeeId/";
    String EMPLOYEE_ID_CHUNKS = EMPLOYEE_ID_DIRECTORY + File.separator + "Chunks/";
    String EMPID_UNCOMPRESSED_BITMAP_FILE_NAME = "empIdUncompressedBitMap.txt";
    String EMPID_COMPRESSED_BITMAP_FILE_NAME = "empIdCompressedBitMap.txt";
    String EMPLOYEE_ID = "employeeId";

    String DEPARTMENT_DIRECTORY = FILE_PATH + File.separator + "Department/";
    String DEPARTMENT_CHUNKS = DEPARTMENT_DIRECTORY + File.separator + "Chunks/";
    String DEPT_UNCOMPRESSED_BITMAP_FILE_NAME = "deptUncompressedBitMap.txt";
    String DEPT_COMPRESSED_BITMAP_FILE_NAME = "deptCompressedBitMap.txt";
    String DEPARTMENT = "department";
    
    String GENDER_UNCOMPRESSED_BITMAP_FILE_NAME = "genderUncompressedBitMap.txt";
    String GENDER_COMPRESSED_BITMAP_FILE_NAME = "genderCompressedBitMap.txt";
}
