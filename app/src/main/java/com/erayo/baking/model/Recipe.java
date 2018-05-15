package com.erayo.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Recipe implements Parcelable{
    public int id;
    public String name;
    public List<Ingredients> ingredients;
    public List<Step> steps;
    public int servings;
    public String image;

    private Recipe(Parcel in){
        id = in.readInt();
        name = in.readString();
        ingredients = in.readArrayList(Ingredients.class.getClassLoader());
        steps = in.readArrayList(Step.class.getClassLoader());
        servings = in.readInt();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
    }
}
