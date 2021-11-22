package team.loser.plantdiseasedetection.models;


public class DiseaseSolution {
    long id;
    String Image1;
    String Image2;
    String Image3;
    String Image4;
    String NameDisease_VN;
    String NameDisease_ENG;
    String Howtocure;
    String Pathogens;
    String Symptom;

    public DiseaseSolution( String image1, String image2, String image3, String image4, String nameDisease_VN, String nameDisease_ENG, String howtocure, String pathogens, String symptom) {

        Image1 = image1;
        Image2 = image2;
        Image3 = image3;
        Image4 = image4;
        NameDisease_VN = nameDisease_VN;
        NameDisease_ENG = nameDisease_ENG;
        Howtocure = howtocure;
        Pathogens = pathogens;
        Symptom = symptom;
    }

    @Override
    public String toString() {
        return "DiseaseSolution{" +
                "NameDisease_VN='" + NameDisease_VN + '\'' +
                '}';
    }



    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public String getImage3() {
        return Image3;
    }

    public void setImage3(String image3) {
        Image3 = image3;
    }

    public String getImage4() {
        return Image4;
    }

    public void setImage4(String image4) {
        Image4 = image4;
    }

    public String getNameDisease_VN() {
        return NameDisease_VN;
    }

    public void setNameDisease_VN(String nameDisease_VN) {
        NameDisease_VN = nameDisease_VN;
    }

    public String getNameDisease_ENG() {
        return NameDisease_ENG;
    }

    public void setNameDisease_ENG(String nameDisease_ENG) {
        NameDisease_ENG = nameDisease_ENG;
    }

    public String getHowtocure() {
        return Howtocure;
    }

    public void setHowtocure(String howtocure) {
        Howtocure = howtocure;
    }

    public String getPathogens() {
        return Pathogens;
    }

    public void setPathogens(String pathogens) {
        Pathogens = pathogens;
    }

    public String getSymptom() {
        return Symptom;
    }

    public void setSymptom(String symptom) {
        Symptom = symptom;
    }
}
