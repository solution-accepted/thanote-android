package edu.uci.thanote.apis.joke;

import edu.uci.thanote.apis.BasicNote;

public class TwoPartJoke extends Joke implements BasicNote {
    private String setup;
    private String delivery;

    public String getSetup() {
        return setup;
    }

    public String getDelivery() {
        return delivery;
    }

    @Override
    public String toString() {
        return super.toString() + "TwoPartJoke{" +
                "setup='" + setup + '\'' +
                ", delivery='" + delivery + '\'' +
                '}';
    }

    @Override
    public String getTitle() {
        return "Joke: " + getCategory();
    }

    @Override
    public String getDetail() {
        return setup + "\n" + delivery;
    }
}
