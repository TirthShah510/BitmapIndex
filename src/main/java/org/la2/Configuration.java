package org.la2;

import java.io.File;

public interface Configuration {

    String FILE_PATH = ".." + File.separator + "Dataset/";
    String FILE_EXTENSION = ".txt";
    String INPUT_FILE_NAME = "dataset";
    String OUTPUT_FILE_NAME = "outputFile";
    String COMPRESSED_DATASET_FILE_NAME = "compressed_";
    String BLOCK_NAME = "Block";
    String MERGED_FILE = "merged";
    String OUTPUT_FILE_ID = "output";

    String EMPID_BITMAP_FILE_NAME = "empIdBitMap";
    String EMPID_COMPRESSED_BITMAP_FILE_NAME = "empIdCompressedBitMap";
    String EMPLOYEE_ID = "employeeId";

    String DEPT_BITMAP_FILE_NAME = "deptBitMap";
    String DEPT_COMPRESSED_BITMAP_FILE_NAME = "deptCompressedBitMap";
    String DEPARTMENT = "department";

    String POSITION_FILE_FOR_TUPLE = "positionForLatestRecord";
    String SORTED_OUTPUT_FILE_NAME = "sortedUniqueTupleFile";

    String GENDER_BITMAP_FILE_NAME = "genderBitMap";
    String GENDER_COMPRESSED_BITMAP_FILE_NAME = "genderCompressedBitMap";
}