package company.whitespace.smartifyandroid.model;

/**
 * Created by Alchemistake on 31/03/2017.
 */
public class Task {
    private String type;
    private String explanation;

    public Task(String type, String explanation) {
        this.type = type;
        this.explanation = explanation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
