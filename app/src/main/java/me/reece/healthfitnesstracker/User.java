package me.reece.healthfitnesstracker;

public class User {

    public String firstName;
    public String getFirstName (){return firstName;}
    public void setFirstName (String name){this.firstName = name;}

    public String surname;
    public String getSurname (){return surname;}
    public void setSurname (String surname){this.surname = surname;}

    public Integer age;
    public Integer getAge (){return age;}
    public void setAge (int age){this.age = age;}

    public Double height;
    public Double getHeight (){return height;}
    public void setHeight (double height){this.height = height;}

    public Double weight;
    public Double getWeight (){return weight;}
    public void setWeight (double weight){this.weight = weight;}


}
