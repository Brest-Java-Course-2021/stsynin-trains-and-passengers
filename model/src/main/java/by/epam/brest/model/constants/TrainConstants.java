package by.epam.brest.model.constants;

public class TrainConstants {

    public static final String TRAIN_ID = "TRAIN_ID";
    public static final String TRAIN_NAME = "TRAIN_NAME";
    public static final String TRAIN_DESTINATION = "TRAIN_DESTINATION";
    public static final String TRAIN_DEPARTURE_DATE = "TRAIN_DEPARTURE_DATE";

    public static final int MAX_TRAIN_NAME_LENGTH = 128;
    public static final int MAX_TRAIN_DESTINATION_NAME_LENGTH = 128;

    public static final String TRAIN_BLANK_NAME_WARN = "The train name cannot be empty.";
    public static final String TRAIN_OVERLONG_NAME_WARN = "The name of the train is too long.";
    public static final String TRAIN_OVERLONG_DESTINATION_NAME_WARN =
            "The name of the train's destination is too long.";
}
