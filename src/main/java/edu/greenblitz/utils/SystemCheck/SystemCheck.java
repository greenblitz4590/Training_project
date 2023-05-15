package edu.greenblitz.utils.SystemCheck;

import edu.greenblitz.tobyDetermined.RobotMap;
import edu.greenblitz.tobyDetermined.subsystems.Battery;
import edu.greenblitz.tobyDetermined.subsystems.GBSubsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import scala.collection.parallel.immutable.ParRange;

import javax.swing.plaf.PanelUI;
import java.util.HashMap;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class SystemCheck extends GBSubsystem{

    private HashMap<GBSubsystem, CheckCommand> subsystemsAndCommands;
    private static SystemCheck instance;


    private ShuffleboardTab tab;
    private double innerBatteryResistance;
    private double startingVoltage;
    public static SystemCheck getInstance(){
        if (instance == null){
            instance = new SystemCheck();
        }
        return instance;
    }

    public SystemCheck(){

        this.innerBatteryResistance = (this.startingVoltage * (Battery.getInstance().getCurrentVoltage()) / Battery.getInstance().getCurrentUsage());

        this.startingVoltage = 13.73;

        subsystemsAndCommands = new HashMap<>();

        this.tab = Shuffleboard.getTab("System check");
        
        
        for (CheckCommand checkCommand : this.subsystemsAndCommands.values()){
            tab.addBoolean(checkCommand.getRunCommand().getRequirements().getClass().getName(),checkCommand.getBooleanSupplier());
        }
        
        tab.addDouble("current voltage", ()-> Battery.getInstance().getCurrentVoltage());
        tab.addDouble("current current", ()-> Battery.getInstance().getCurrentUsage());
        tab.addDouble("battery inner resistance:", () -> getInnerBatteryResistance());
        tab.addDouble("battery voltage drop:", () ->  SystemCheck.getInstance().getStartingVoltage() - Battery.getInstance().getCurrentVoltage());
        
    }
    
    public double getInnerBatteryResistance() {
        this.innerBatteryResistance  = (this.startingVoltage * (Battery.getInstance().getCurrentVoltage()) / Battery.getInstance().getCurrentUsage());
        return innerBatteryResistance;
    }

    public double getStartingVoltage() {
        return startingVoltage;
    }

    public void add (GBSubsystem subsystem, CheckCommand checkCommand){
        subsystemsAndCommands.putIfAbsent(subsystem, checkCommand);
        this.tab.addBoolean(checkCommand.getRunCommand().getRequirements().getClass().getName(),checkCommand.getBooleanSupplier());
    }

    public void remove (GBSubsystem subsystem,Command command){
        subsystemsAndCommands.remove(subsystem,command);
    }

    public CheckCommand getCheckCommandForSubsystem(GBSubsystem subsystem){
        return subsystemsAndCommands.get(subsystem);
    }
    public GBSubsystem[] getSubsystems (){
        return subsystemsAndCommands.keySet().toArray(new GBSubsystem[0]);
    }

}
