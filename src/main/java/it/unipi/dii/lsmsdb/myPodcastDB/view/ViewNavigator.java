package it.unipi.dii.lsmsdb.myPodcastDB.view;

public enum ViewNavigator {
    HOMEPAGE {
        @Override
        public String getPage() {
            return "HomePage.fxml";
        }
    },
    AUTHORPROFILE {
        @Override
        public String getPage() {
            return "AuthorProfile.fxml";
        }
    },
    LOGIN{
         @Override
         public String getPage(){ return "Login.fxml";}
    },
    SIGNUP{
        @Override
        public String getPage(){ return "SignUP.fxml";}
    },
    USERPAGE {
        @Override
        public String getPage() {
            return "UserPage.fxml";
        }
    },
    SEARCH {
        @Override
        public String getPage() {
            return "Search.fxml";
        }
    },

    PODCASTPAGE {
        @Override
        public String getPage() { return "PodcastPage.fxml"; }
    },

    REVIEW {
        @Override
        public String getPage() { return "ReviewPage.fxml"; }
    },
    ADMINDASHBOARD {
        @Override
        public String getPage() { return "AdminDashboard.fxml"; }
    };

    public abstract String getPage();

}
