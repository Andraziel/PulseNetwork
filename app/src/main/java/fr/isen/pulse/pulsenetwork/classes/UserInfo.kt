package fr.isen.pulse.pulsenetwork.classes

import com.google.gson.annotations.SerializedName;

public class UserInfo (
    @SerializedName("idUser") var idUser  : String? = null,
    @SerializedName("firstName") var firstName : String? = null,
    @SerializedName("lastName") var lastName          : String? = null

):java.io.Serializable
