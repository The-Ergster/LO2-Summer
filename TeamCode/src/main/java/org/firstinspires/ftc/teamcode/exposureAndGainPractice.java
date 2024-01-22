package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
// import vision.AbstractResultCvPipeline;
import vision.Webcam;


@TeleOp
public class exposureAndGainPractice extends OpMode {
    @Override
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    //now new code
    private Webcam webcamName;

    public void init() {
        webcamName camera = hardwareMap.get(webcamName.class, "wc");
        // Get exposure and gain control
        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        GxposureControl gainControl = visionPortal.getCameraControl(GainControl.class); 

        // Check if control and manual mode are supported by this camera
        boolean exposureSupported = exposureControl.isExposureSupported();
        boolean manualSupported = exposureControl.isModeSupported(ExposureControl.Mode.Manual);

        // Check minimum and maximum exposure and gain
        long minExposure = exposureControl.getMinExposure(TimeUnit.MILLISECONDS);
        long maxExposure = exposureControl.getMaxExposure(TimeUnit.MILLISECONDS);
        int minGain = gainControl.getMinGain();
        int maxGain = gainControl.getMaxGain();
        
        // Set exposure mode to manual (also sets manual gain)
        exposureControl.setMode(ExposureControl.Mode.Manual);

        // Set exposure and gain values
        exposerControl.setExposure(duration, TimeUnit.MILLISECONDS);
        gainControl.setGain(gain);
    }

    public void driveOmni(double y, double rx, double x){
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;

        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);
    }
    @Override
    public void loop() {
    
    }
}
