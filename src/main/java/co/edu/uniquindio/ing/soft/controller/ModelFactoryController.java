package co.edu.uniquindio.ing.soft.controller;

public class ModelFactoryController {

    private static class SingletonHolder {
        private static final ModelFactoryController INSTANCE = new ModelFactoryController();
    }

    public static ModelFactoryController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ModelFactoryController() {

    }
}
