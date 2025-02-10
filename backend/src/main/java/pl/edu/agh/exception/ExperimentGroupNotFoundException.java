package pl.edu.agh.exception;

import org.bson.types.ObjectId;

public class ExperimentGroupNotFoundException extends Exception {
    public ExperimentGroupNotFoundException(String groupName) {
        super("Experiment group with groupName=[" + groupName + "] was not found.");
    }
}
