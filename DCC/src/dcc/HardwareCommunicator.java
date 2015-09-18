package dcc;

import dcc.DCCUtils.SpeedMode;
import dcc.packets.CVProgramPacket;
import dcc.packets.FunctionPacket;
import dcc.packets.HardResetPacket;
import dcc.packets.MovementPacket;
import java.util.HashMap;
import java.util.Map;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author mjrb5
 */
public class HardwareCommunicator implements DCCCommunicator {

    private final Map<DCCAddress, CommandContainer> commands = new HashMap<>();
    private volatile SerialPort serialPort;
    private Thread workerThread;

    public HardwareCommunicator(String portName) {
        serialPort = new SerialPort(portName);
    }

    public void start() {
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
                try {
                    while (true) {
                        synchronized (commands) {
                            if (!commands.isEmpty()) {
                                DCCAddress addr = commands.keySet().iterator().next();
                                CommandContainer comms = commands.get(addr);
                                for (String command : comms.getCommands()) {
                                    serialPort.writeString(command);
                                    System.out.println(command);
                                }
                                commands.remove(addr);
                            }
                        }
                        Thread.sleep(10);
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
        synchronized (commands) {
            CommandContainer container = commands.get(packet.getAddress());
            if (container == null) {
                container = new CommandContainer();
            }
            container.setMovementCommand(movementPacketToCommand(packet));
            commands.put(packet.getAddress(), container);
        }
    }

    @Override
    public void setFunction(FunctionPacket packet) {
        synchronized (commands) {
            CommandContainer container = commands.get(packet.getAddress());
            if (container == null) {
                container = new CommandContainer();
            }
            container.setFunctionCommand(functionPacketToCommand(packet));
            commands.put(packet.getAddress(), container);
        }
    }

    @Override
    public void reset(HardResetPacket packet) {
        synchronized (commands) {
            if (packet.getAddress().isBroadcast()) {
                commands.clear();
            }
            CommandContainer container = commands.get(packet.getAddress());
            if (container == null) {
                container = new CommandContainer();
            }
            container.setResetCommand(resetPacketToCommand(packet));
            commands.put(packet.getAddress(), container);
        }
    }

    @Override
    public void programCV(CVProgramPacket packet) {
        synchronized (commands) {
            CommandContainer container = commands.get(packet.getAddress());
            if (container == null) {
                container = new CommandContainer();
            }
            container.setProgramCommand(programCVPacketToCommand(packet));
            commands.put(packet.getAddress(), container);
        }
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
        ret.append(packet.getAddress().getAddressTypeAsInt());
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
        return "func," + packet.getAddress().getAddress() + "," + packet.getAddress().getAddressTypeAsInt() + "," + packet.getBitMask() + ";";
    }

    private String resetPacketToCommand(HardResetPacket packet) {
        return "reset," + packet.getAddress().getAddress() + "," + packet.getAddress().getAddressTypeAsInt() + ";";
    }

    private String programCVPacketToCommand(CVProgramPacket packet) {
        StringBuilder ret = new StringBuilder("prog,");
        ret.append(packet.getAddress().getAddress());
        ret.append(",");
        ret.append(packet.getAddress().getAddressTypeAsInt());
        ret.append(",");
        ret.append(packet.getCvAddress());
        ret.append(",");
        ret.append(packet.getCvNewVal());
        ret.append(";");
        return ret.toString();
    }

}
