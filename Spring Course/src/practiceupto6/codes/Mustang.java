package practiceupto6.codes;

public class Mustang implements Car{

    String engineSound;
    double engineVolume;
    String model;

    public Mustang(double engineVolume, String model){
        this.engineVolume = engineVolume;
        this.model = model;
    }

    @Override
    public String getEngineSound() {
        return engineSound;
    }

    public void setEngineSound(String engineSound) {
        this.engineSound = engineSound;
    }

    public double getEngineVolume() {
        return engineVolume;
    }

    public String getModel() {
        return model;
    }
}
