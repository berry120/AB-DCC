package dcc;

import dcc.DCCUtils.SpeedMode;
import dcc.packets.CVProgramPacket;
import dcc.packets.FunctionPacket;
import dcc.packets.HardResetPacket;
import dcc.packets.MovementPacket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author mjrb5
 */
public class HardwareCommunicator implements DCCCommunicator {

    private volatile Map<DCCAddress, String> movementCommands;
    private Map<DCCAddress, String> functionCommands;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(128);
    private volatile SerialPort serialPort;
    private Thread workerThread;
    private Thread queueSubmitThread;

    public HardwareCommunicator(String portName) {
        movementCommands = Collections.synchronizedMap(new HashMap<>());
        functionCommands = Collections.synchronizedMap(new HashMap<>());
        serialPort = new SerialPort(portName);
    }

    public void start() {
        queueSubmitThread = new Thread() {
            public void run() {
                try {
                    while(true) {
                        Set<String> commands = new HashSet<>();
                        commands.addAll(movementCommands.values());
                        commands.addAll(functionCommands.values());
                        for(String cmd : commands) {
                            queue.add(cmd);
                            Thread.sleep(50);
                        }
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
        queueSubmitThread.setDaemon(true);
        
        workerThread = new Thread() {
            public void run() {
                try {
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (SerialPortException ex) {
                    ex.printStackTrace();
                }
                queueSubmitThread.start();
                try {
                    while (true) {
                        String command = queue.take();
                        System.out.println(command);
                        serialPort.writeString(command);
                    }
                } catch (InterruptedException | SerialPortException ex) {
                    ex.printStackTrace();
                }
            }
        };
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void stop() {
        workerThread.interrupt();
        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setMovement(MovementPacket packet) {
        movementCommands.put(packet.getAddress(), movementPacketToCommand(packet));
    }

    @Override
    public void setFunction(FunctionPacket packet) {
        functionCommands.put(packet.getAddress(), functionPacketToCommand(packet));
    }

    @Override
    public void reset(HardResetPacket packet) {
        movementCommands.remove(packet.getAddress());
        functionCommands.remove(packet.getAddress());
        queue.add(resetPacketToCommand(packet));
    }

    @Override
    public void programCV(CVProgramPacket packet) {
        queue.add(programCVPacketToCommand(packet));
    }

    private String movementPacketToCommand(MovementPacket packet) {
        StringBuilder ret = new StringBuilder("spd,");
        switch (packet.getSpeedMode()) {
            case SPEED_14:
                ret.append("14,");
                break;
            case SPEED_28:
                ret.append("28,");
                break;
            case SPEED_128:
                ret.append("128,");
                break;
        }
        ret.append(packet.getAddress().getAddress());
        ret.append(",");
        ret.append(speedFromPacket(packet));
        ret.append(";");
        return ret.toString();
    }

    private String speedFromPacket(MovementPacket packet) {
        if (packet.isEmergencyStop()) {
            return "estop";
        } else if (packet.isStop() | packet.getSpeed() == 0) {
            return "stop";
        } else if (packet.getSpeedMode() == SpeedMode.SPEED_14) {
            int speed = DCCUtils.getSpeedForMode(packet.getSpeed(), DCCUtils.SpeedMode.SPEED_14);
            if (packet.getDirection() == DCCUtils.Direction.REVERSE) {
                speed *= -1;
            }
            return Integer.toString(speed);
        } else if (packet.getSpeedMode() == SpeedMode.SPEED_28) {
            int speed = DCCUtils.getSpeedForMode(packet.getSpeed(), DCCUtils.SpeedMode.SPEED_28);
            if (packet.getDirection() == DCCUtils.Direction.REVERSE) {
                speed *= -1;
            }
            return Integer.toString(speed);
        } else if (packet.getSpeedMode() == SpeedMode.SPEED_128) {
            int speed = DCCUtils.getSpeedForMode(packet.getSpeed(), DCCUtils.SpeedMode.SPEED_128);
            if (packet.getDirection() == DCCUtils.Direction.REVERSE) {
                speed *= -1;
            }
            return Integer.toString(speed);
        } else {
            return null;
        }
    }

    private String functionPacketToCommand(FunctionPacket packet) {
        return "func," + packet.getAddress().getAddress() + "," + packet.getBitMask() + ";";
    }

    private String resetPacketToCommand(HardResetPacket packet) {
        return "reset," + packet.getAddress().getAddress() + ";";
    }

    private String programCVPacketToCommand(CVProgramPacket packet) {
        StringBuilder ret = new StringBuilder("prog,");
        ret.append(packet.getAddress().getAddress());
        ret.append(",");
        ret.append(packet.getCvAddress());
        ret.append(",");
        ret.append(packet.getCvNewVal());
        ret.append(";");
        return ret.toString();
    }

}
