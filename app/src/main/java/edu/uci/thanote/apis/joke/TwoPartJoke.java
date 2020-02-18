package edu.uci.thanote.apis.joke;

public class TwoPartJoke extends Joke {
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
}
