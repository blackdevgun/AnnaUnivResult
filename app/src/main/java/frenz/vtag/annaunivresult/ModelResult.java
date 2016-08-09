package frenz.vtag.annaunivresult;

/**
 * Created by gunalan on 8/8/16.
 */
public class ModelResult {

    private String itemOne;
    private String itemTwo;
    private String itemThree;

    boolean resultHeader;

    public ModelResult(String itemOne, String itemTwo, String itemThree, boolean resultHeader) {
        this.itemOne = itemOne;
        this.itemTwo = itemTwo;
        this.itemThree = itemThree;
        this.resultHeader = resultHeader;
    }

    public String getItemOne() {
        return itemOne;
    }

    public void setItemOne(String itemOne) {
        this.itemOne = itemOne;
    }

    public String getItemTwo() {
        return itemTwo;
    }

    public void setItemTwo(String itemTwo) {
        this.itemTwo = itemTwo;
    }

    public String getItemThree() {
        return itemThree;
    }

    public void setItemThree(String itemThree) {
        this.itemThree = itemThree;
    }

    public boolean isResultHeader() {
        return resultHeader;
    }

    public void setResultHeader(boolean resultHeader) {
        this.resultHeader = resultHeader;
    }
}
