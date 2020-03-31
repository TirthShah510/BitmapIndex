package org.la2;

import java.io.File;

public interface Configuration {

    String FILE_PATH = ".." + File.separator + "Dataset/";
    String INPUT_FILE_NAME = "dataset.txt";
    String COMPRESSED_DATASET_FILE_NAME = "compressed_" + Configuration.INPUT_FILE_NAME;
    String BLOCK_NAME = "Block";

    String EMPLOYEE_ID_DIRECTORY = FILE_PATH + File.separator + "EmployeeId/";
    String EMPLOYEE_ID_CHUNKS = EMPLOYEE_ID_DIRECTORY + File.separator + "Chunks/";
    String EMPLOYEE_ID = "employeeId";

    String DEPARTMENT_DIRECTORY = FILE_PATH + File.separator + "Department/";
    String DEPARTMENT_CHUNKS = DEPARTMENT_DIRECTORY + File.separator + "Chunks/";
    String DEPARTMENT = "department";

    String POSITION_FILE_FOR_TUPLE = "positionForLatestRecord.txt";
    String OUTPUT_FILE_NAME = "output.txt";
}
