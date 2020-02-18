package edu.uci.thanote.apis.joke;

public class Joke {
    private int id;
    private String category;
    private String type;
    private boolean error;
    private Flag flags;

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public boolean isError() {
        return error;
    }

    public Flag getFlags() {
        return flags;
    }

    class Flag {
        private boolean nsfw;
        private boolean religious;
        private boolean political;
        private boolean racist;
        private boolean sexist;

        public boolean isNsfw() {
            return nsfw;
        }

        public boolean isReligious() {
            return religious;
        }

        public boolean isPolitical() {
            return political;
        }

        public boolean isRacist() {
            return racist;
        }

        public boolean isSexist() {
            return sexist;
        }
    }

    @Override
    public String toString() {
        return "Joke{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", error=" + error +
                ", flags=" + flags +
                '}';
    }
}
