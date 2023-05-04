package com.example.loco_v1;

public class Model_MarketPlaceItem {


        private String id;
        private String type;
        private String itemDescription;
        private int price;
        private String time;
        private String imageUrl;
        private String itemName;

        private String category;
        private String user_id;
        private boolean sold;

        public Model_MarketPlaceItem() {
            // Default constructor required for calls to DataSnapshot.getValue(LostAndFound.class)
        }

        public Model_MarketPlaceItem(String id, String itemDescription, String location, String time, String imageUrl,String user_id,boolean sold,String category) {
            this.id = id;
            this.itemDescription = itemDescription;
            this.time = time;
            this.imageUrl = imageUrl;
            this.user_id=user_id;
            this.sold=sold;
            this.category=category;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
        return id;
    }

        public void setUserId(String id) {
        this.id = id;
    }

        public Boolean getSold() {
            return sold;
        }

        public void setSold(boolean sold) {
            this.sold =sold;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price=price;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemName() {
            return itemName;
        }



}
