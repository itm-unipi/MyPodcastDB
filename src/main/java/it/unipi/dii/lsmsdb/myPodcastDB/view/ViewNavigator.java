package it.unipi.dii.lsmsdb.myPodcastDB.view;

public enum ViewNavigator {
    TEST {
        @Override
        public String getPage() {
            return "Test.fxml";
        }
    },
    TEST2 {
        @Override
        public String getPage() {
            return "Test2.fxml";
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
    USERPAGE{
        @Override
        public String getPage(){ return "UserPage.fxml";}
    };

    public abstract String getPage();

}
