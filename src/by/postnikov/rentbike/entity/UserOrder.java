package by.postnikov.rentbike.entity;

public class UserOrder {
	
	private User user;
	private BikeOrder bikeOrder;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public BikeOrder getBikeOrder() {
		return bikeOrder;
	}
	public void setBikeOrder(BikeOrder bikeOrder) {
		this.bikeOrder = bikeOrder;
	}

}
