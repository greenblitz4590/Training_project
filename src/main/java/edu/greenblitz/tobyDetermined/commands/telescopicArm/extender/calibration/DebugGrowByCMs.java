package edu.greenblitz.tobyDetermined.commands.telescopicArm.extender.calibration;

import edu.greenblitz.tobyDetermined.commands.telescopicArm.extender.ExtendToLength;
import edu.greenblitz.tobyDetermined.subsystems.telescopicArm.Extender;

public class DebugGrowByCMs extends ExtendToLength {

	public DebugGrowByCMs(double lengthInCM) {
		super(Extender.getInstance().getLength()+ lengthInCM/100);
	}
}
