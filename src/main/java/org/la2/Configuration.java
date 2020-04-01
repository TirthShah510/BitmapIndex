package org.la2;

import java.io.File;

public interface Configuration {

    String FILE_PATH = ".." + File.separator + "Dataset/";
    String FILE_EXTENSION = ".txt";
    String INPUT_FILE_NAME = "dataset";
    String COMPRESSED_DATASET_FILE_NAME = "compressed_" + Configuration.INPUT_FILE_NAME + Configuration.FILE_EXTENSION;
    String BLOCK_NAME = "Block";

    String EMPID_COMPRESSED_BITMAP_FILE_NAME = "empIdCompressedBitMap";
    String EMPLOYEE_ID = "employeeId";

    String DEPT_COMPRESSED_BITMAP_FILE_NAME = "deptCompressedBitMap";
    String DEPARTMENT = "department";

    String POSITION_FILE_FOR_TUPLE = "positionForLatestRecord";
    String SORTED_OUTPUT_FILE_NAME = "sortedUniqueTupleFile";

    String GENDER_COMPRESSED_BITMAP_FILE_NAME = "genderCompressedBitMap";

}
