package com.example.loco_v1;

public class Model_MarketPlaceItem {
        private String id;
        private String type;
        private String itemDescription;
        private String price;
        private String time;
        private String imageUrl;
        private Boolean isLost;
        private String itemName;

        public Model_MarketPlaceItem() {
            // Default constructor required for calls to DataSnapshot.getValue(LostAndFound.class)
        }

        public Model_MarketPlaceItem(String id, String type, String itemDescription, String location, String time, String imageUrl) {
            this.id = id;
            this.type = type;
            this.itemDescription = itemDescription;
            this.price = location;
            this.time = time;
            this.imageUrl = imageUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
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

        public void setLost(boolean isLost) {
            this.isLost = isLost;
        }
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemName() {
            return itemName;
        }



}
