package it.unipi.dii.lsmsdb.myPodcastDB.view;

public enum ViewNavigator {
    USERHOME {
        @Override
        public String getPage() {
            return "UserHome.fxml";
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
    HOMEUSER {
        @Override
        public String getPage() {
            return "HomeUser.fxml";
        }
    };

    public abstract String getPage();

}
