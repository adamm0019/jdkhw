/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.hardwareinfo;

import java.awt.Desktop;
import java.net.URI;
import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.hardware.*;
import oshi.util.FormatUtil;

/**
 *
 * @author adam-
 */
public class HardwareDetectGUI extends javax.swing.JFrame {

    /**
     * Creates new form FlightBookingGUI
     */
    public HardwareDetectGUI() {
        initComponents();
        
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArchitecture = System.getProperty("os.arch");
        String winUserName = System.getProperty("user.name");

        osInfoTxtArea.setText("""
                OS Info:
                """ + osName + "\n" + "Version: " + osVersion + "\n" + "Architecture: " + osArchitecture + "\n\n" + "User: " + winUserName);

        SystemInfo si = new SystemInfo();

        OperatingSystem os = si.getOperatingSystem();
        CentralProcessor processor = si.getHardware().getProcessor();
        HardwareAbstractionLayer hal = si.getHardware();
        GlobalMemory memory = si.getHardware().getMemory();
        List<GraphicsCard> graphics = hal.getGraphicsCards();
        List<HWDiskStore> diskStores = hal.getDiskStores();
        List<NetworkIF> networks = hal.getNetworkIFs();

        StringBuilder sb = new StringBuilder();
        for (CentralProcessor.ProcessorCache cache : processor.getProcessorCaches()) {
            sb.append(String.format("Cache Type: %s%n", cache.getType()));
            sb.append(String.format("Cache Size: %d%n", cache.getCacheSize()));
            sb.append(String.format("Cache Associativity: %d%n", cache.getAssociativity()));
            sb.append("\n");
        }
        cacheField.setText(sb.toString());

        long bytes = memory.getTotal();

        for (GraphicsCard graphicsCard : graphics) {
            String gpuName = graphicsCard.getName();
            gpuNameField.addItem(graphicsCard.getName());
        }

        for (HWDiskStore diskStore : diskStores) {
            // get disk model from OS
            String diskModel = diskStore.getModel();
            diskNameField.addItem(diskModel);
        }

        for (NetworkIF net : networks) {
            String networkName = net.getDisplayName();
            networkNameField.addItem(networkName);
        }

        double gigabytes = bytes / 1073741824.0;

        CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
        String cpuModel = processorIdentifier.getName();

        int coreIdentifier = processor.getLogicalProcessorCount();

        logicalCores.setText(coreIdentifier + " Cores");

        // convert bytes to gigabytes
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedRam = df.format(gigabytes);

        // display the total ram
        memorySize.setText(formattedRam + " GB");
        cpuNameTxtField.setBorder(null);
        memoryManufacturer.setBorder(null);
        memoryPercent.setBorder(null);
        memorySize.setBorder(null);
        memSpeed.setBorder(null);
        gpuNameField.setBorder(null);
        logicalCores.setBorder(null);
        cpuPercent.setBorder(null);
        memoryType.setBorder(null);
        gpuVRAMField.setBorder(null);
        gpuVendorField.setBorder(null);
        gpuVersion.setBorder(null);
        diskDriveModelField.setBorder(null);
        diskDriveSizeField.setBorder(null);
        diskDriveSerialField.setBorder(null);
        cacheField.setBorder(null);
        bankTxtField.setBorder(null);

        cpuNameTxtField.setText(cpuModel);

        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory globalMemory = hardware.getMemory();

        List<PhysicalMemory> physicalMemories = globalMemory.getPhysicalMemory();
        for (PhysicalMemory physicalMemory : physicalMemories) {
            memoryManufacturer.setText(physicalMemory.getManufacturer());
            memoryType.setText(physicalMemory.getMemoryType());
            memSpeed.setText(FormatUtil.formatHertz(physicalMemory.getClockSpeed()));
            bankTxtField.setText("Module(s) in " + physicalMemory.getBankLabel());
        }

        // get cpu usage
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                double usage = processor.getSystemCpuLoad(500);
                int percentage = (int) Math.round(usage * 100);
                cpuPercent.setText(percentage + "%");
            }
        }, 0, 2, TimeUnit.SECONDS);

        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        executor2.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long usedMem = globalMemory.getTotal() - globalMemory.getAvailable();
                double usage = (double) usedMem / globalMemory.getTotal();
                int percentage = (int) Math.round(usage * 100);
                memoryPercent.setText(percentage + "%");
            }
        }, 0, 2, TimeUnit.SECONDS);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hardwareInfoPane = new javax.swing.JTabbedPane();
        cpuPanel = new javax.swing.JPanel();
        cpuUsageLbl = new javax.swing.JLabel();
        cpuNameTxtField = new javax.swing.JFormattedTextField();
        cpuNameLbl = new javax.swing.JLabel();
        logicalLbl = new javax.swing.JLabel();
        logicalCores = new javax.swing.JFormattedTextField();
        cpuPercent = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        cpuTempLbl = new javax.swing.JLabel();
        cacheScrlPne = new javax.swing.JScrollPane();
        cacheField = new javax.swing.JTextArea();
        memoryPanel = new javax.swing.JPanel();
        memUsageLbl = new javax.swing.JLabel();
        memTypeLbl = new javax.swing.JLabel();
        memoryManufacturer = new javax.swing.JFormattedTextField();
        memorySizeLbl = new javax.swing.JLabel();
        memorySize = new javax.swing.JFormattedTextField();
        memoryPercent = new javax.swing.JFormattedTextField();
        memoryType = new javax.swing.JFormattedTextField();
        memTypeLbl1 = new javax.swing.JLabel();
        bankLbl = new javax.swing.JLabel();
        memSpeed = new javax.swing.JFormattedTextField();
        jSeparator2 = new javax.swing.JSeparator();
        memSpeedLbl1 = new javax.swing.JLabel();
        bankTxtField = new javax.swing.JFormattedTextField();
        gpuPanel = new javax.swing.JPanel();
        gpuNameLbl = new javax.swing.JLabel();
        gpuNameField = new javax.swing.JComboBox<>();
        gpuVramLbl = new javax.swing.JLabel();
        gpuVendorLbl = new javax.swing.JLabel();
        gpuVendorField = new javax.swing.JFormattedTextField();
        gpuVRAMField = new javax.swing.JFormattedTextField();
        gpuVersionLbl = new javax.swing.JLabel();
        gpuVersion = new javax.swing.JFormattedTextField();
        diskPanel = new javax.swing.JPanel();
        diskNameField = new javax.swing.JComboBox<>();
        diskLbl = new javax.swing.JLabel();
        diskDriveModelField = new javax.swing.JFormattedTextField();
        diskDriveSizeField = new javax.swing.JFormattedTextField();
        diskDriveSerialField = new javax.swing.JFormattedTextField();
        modeLbl = new javax.swing.JLabel();
        diskSizeLbl = new javax.swing.JLabel();
        diskSerialLbl = new javax.swing.JLabel();
        networkPanel = new javax.swing.JPanel();
        networkNameLbl = new javax.swing.JLabel();
        macAddressLbl = new javax.swing.JLabel();
        ipv4Lbl = new javax.swing.JLabel();
        ipv6Lbl = new javax.swing.JLabel();
        macNameField = new javax.swing.JFormattedTextField();
        ipv4NameField = new javax.swing.JFormattedTextField();
        ipv6NameField = new javax.swing.JFormattedTextField();
        networkNameField = new javax.swing.JComboBox<>();
        netSpeedLbl = new javax.swing.JLabel();
        netSpeedFieldName = new javax.swing.JFormattedTextField();
        netActivityBtn = new javax.swing.JButton();
        osPanel = new javax.swing.JPanel();
        osInfoTxtArea = new javax.swing.JTextArea();
        aboutPanel = new javax.swing.JPanel();
        crAM = new javax.swing.JLabel();
        madeWith = new javax.swing.JLabel();
        oshiLink = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("jdkhw");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        hardwareInfoPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hardwareInfoPaneStateChanged(evt);
            }
        });

        cpuUsageLbl.setText("CPU Usage:");

        cpuNameTxtField.setEditable(false);
        cpuNameTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cpuNameTxtFieldActionPerformed(evt);
            }
        });

        cpuNameLbl.setText("CPU Name:");

        logicalLbl.setText("Logical Cores:");

        logicalCores.setEditable(false);

        cpuPercent.setEditable(false);

        cpuTempLbl.setText("Cache Info:");

        cacheField.setEditable(false);
        cacheField.setColumns(20);
        cacheField.setRows(5);
        cacheScrlPne.setViewportView(cacheField);

        javax.swing.GroupLayout cpuPanelLayout = new javax.swing.GroupLayout(cpuPanel);
        cpuPanel.setLayout(cpuPanelLayout);
        cpuPanelLayout.setHorizontalGroup(
            cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(cpuPanelLayout.createSequentialGroup()
                        .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(logicalLbl)
                            .addComponent(cpuUsageLbl)
                            .addComponent(cpuNameLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cpuNameTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
                            .addComponent(logicalCores)
                            .addComponent(cpuPercent, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(cpuPanelLayout.createSequentialGroup()
                        .addComponent(cpuTempLbl)
                        .addGap(18, 18, 18)
                        .addComponent(cacheScrlPne)))
                .addContainerGap())
        );
        cpuPanelLayout.setVerticalGroup(
            cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cpuNameTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cpuNameLbl))
                .addGap(18, 18, 18)
                .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(logicalLbl)
                    .addComponent(logicalCores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cpuUsageLbl)
                    .addComponent(cpuPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cpuPanelLayout.createSequentialGroup()
                        .addComponent(cpuTempLbl)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cacheScrlPne, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                .addContainerGap())
        );

        hardwareInfoPane.addTab("CPU", cpuPanel);

        memUsageLbl.setText("Memory Usage:");

        memTypeLbl.setText("Manufacturer:");

        memoryManufacturer.setEditable(false);

        memorySizeLbl.setText("Memory Size:");

        memorySize.setEditable(false);

        memoryPercent.setEditable(false);

        memoryType.setEditable(false);

        memTypeLbl1.setText("Memory Type:");

        bankLbl.setText("Memory Bank:");

        memSpeed.setEditable(false);

        memSpeedLbl1.setText("Memory Speed:");

        bankTxtField.setEditable(false);

        javax.swing.GroupLayout memoryPanelLayout = new javax.swing.GroupLayout(memoryPanel);
        memoryPanel.setLayout(memoryPanelLayout);
        memoryPanelLayout.setHorizontalGroup(
            memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(memoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(memoryPanelLayout.createSequentialGroup()
                        .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(memUsageLbl)
                            .addComponent(memorySizeLbl))
                        .addGap(18, 18, 18)
                        .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(memorySize)
                            .addComponent(memoryPercent)))
                    .addGroup(memoryPanelLayout.createSequentialGroup()
                        .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(memTypeLbl)
                            .addComponent(memTypeLbl1))
                        .addGap(25, 25, 25)
                        .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(memoryType, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(memoryManufacturer, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)))
                    .addGroup(memoryPanelLayout.createSequentialGroup()
                        .addComponent(memSpeedLbl1)
                        .addGap(18, 18, 18)
                        .addComponent(memSpeed))
                    .addGroup(memoryPanelLayout.createSequentialGroup()
                        .addComponent(bankLbl)
                        .addGap(24, 24, 24)
                        .addComponent(bankTxtField)))
                .addContainerGap())
        );
        memoryPanelLayout.setVerticalGroup(
            memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(memoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memorySizeLbl)
                    .addComponent(memorySize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memUsageLbl)
                    .addComponent(memoryPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memTypeLbl)
                    .addComponent(memoryManufacturer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memoryType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(memTypeLbl1))
                .addGap(18, 18, 18)
                .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(memSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(memSpeedLbl1))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(memoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bankLbl)
                    .addComponent(bankTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        hardwareInfoPane.addTab("Memory", memoryPanel);

        gpuNameLbl.setText("GPU:");

        gpuNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gpuNameFieldActionPerformed(evt);
            }
        });

        gpuVramLbl.setText("VRAM:");

        gpuVendorLbl.setText("Vendor:");

        gpuVendorField.setEditable(false);
        gpuVendorField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gpuVendorFieldActionPerformed(evt);
            }
        });

        gpuVRAMField.setEditable(false);

        gpuVersionLbl.setText("Version:");

        gpuVersion.setEditable(false);
        gpuVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gpuVersionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gpuPanelLayout = new javax.swing.GroupLayout(gpuPanel);
        gpuPanel.setLayout(gpuPanelLayout);
        gpuPanelLayout.setHorizontalGroup(
            gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gpuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gpuVendorLbl)
                    .addComponent(gpuNameLbl)
                    .addComponent(gpuVramLbl)
                    .addComponent(gpuVersionLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(gpuVRAMField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gpuVendorField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gpuNameField, javax.swing.GroupLayout.Alignment.LEADING, 0, 509, Short.MAX_VALUE)
                    .addComponent(gpuVersion))
                .addGap(352, 352, 352))
        );
        gpuPanelLayout.setVerticalGroup(
            gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gpuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gpuNameLbl)
                    .addComponent(gpuNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gpuVendorLbl)
                    .addComponent(gpuVendorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gpuVramLbl)
                    .addComponent(gpuVRAMField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(gpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gpuVersionLbl)
                    .addComponent(gpuVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        hardwareInfoPane.addTab("GPU", gpuPanel);

        diskNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diskNameFieldActionPerformed(evt);
            }
        });

        diskLbl.setText("Disk:");

        diskDriveModelField.setEditable(false);

        diskDriveSizeField.setEditable(false);

        diskDriveSerialField.setEditable(false);

        modeLbl.setText("Model:");

        diskSizeLbl.setText("Size:");

        diskSerialLbl.setText("Serial:");

        javax.swing.GroupLayout diskPanelLayout = new javax.swing.GroupLayout(diskPanel);
        diskPanel.setLayout(diskPanelLayout);
        diskPanelLayout.setHorizontalGroup(
            diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, diskPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(diskLbl)
                    .addComponent(modeLbl)
                    .addComponent(diskSizeLbl)
                    .addComponent(diskSerialLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(diskDriveSizeField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(diskDriveModelField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(diskNameField, javax.swing.GroupLayout.Alignment.TRAILING, 0, 830, Short.MAX_VALUE)
                    .addComponent(diskDriveSerialField))
                .addContainerGap())
        );
        diskPanelLayout.setVerticalGroup(
            diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(diskPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diskNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diskLbl))
                .addGap(18, 18, 18)
                .addGroup(diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diskDriveModelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modeLbl))
                .addGap(18, 18, 18)
                .addGroup(diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diskDriveSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diskSizeLbl))
                .addGap(18, 18, 18)
                .addGroup(diskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diskDriveSerialField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(diskSerialLbl))
                .addContainerGap(142, Short.MAX_VALUE))
        );

        hardwareInfoPane.addTab("Disk", diskPanel);

        networkNameLbl.setText("Network:");

        macAddressLbl.setText("MAC Address:");

        ipv4Lbl.setText("IPv4:");

        ipv6Lbl.setText("IPv6:");

        macNameField.setEditable(false);

        ipv4NameField.setEditable(false);

        ipv6NameField.setEditable(false);

        networkNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                networkNameFieldActionPerformed(evt);
            }
        });

        netSpeedLbl.setText("Speed:");

        netSpeedFieldName.setEditable(false);

        netActivityBtn.setText("Network Activity");
        netActivityBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netActivityBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout networkPanelLayout = new javax.swing.GroupLayout(networkPanel);
        networkPanel.setLayout(networkPanelLayout);
        networkPanelLayout.setHorizontalGroup(
            networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(networkPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(macAddressLbl)
                    .addComponent(networkNameLbl)
                    .addComponent(ipv4Lbl)
                    .addComponent(ipv6Lbl)
                    .addComponent(netSpeedLbl))
                .addGap(18, 18, 18)
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ipv6NameField)
                    .addComponent(ipv4NameField)
                    .addComponent(macNameField)
                    .addComponent(networkNameField, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(netSpeedFieldName))
                .addGap(39, 39, 39))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, networkPanelLayout.createSequentialGroup()
                .addContainerGap(422, Short.MAX_VALUE)
                .addComponent(netActivityBtn)
                .addGap(402, 402, 402))
        );
        networkPanelLayout.setVerticalGroup(
            networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(networkPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(networkNameLbl)
                    .addComponent(networkNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(macAddressLbl)
                    .addComponent(macNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipv4Lbl)
                    .addComponent(ipv4NameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipv6Lbl)
                    .addComponent(ipv6NameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(networkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(netSpeedLbl)
                    .addComponent(netSpeedFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(netActivityBtn)
                .addContainerGap())
        );

        hardwareInfoPane.addTab("Network", networkPanel);

        osInfoTxtArea.setEditable(false);
        osInfoTxtArea.setBackground(new java.awt.Color(240, 240, 240));
        osInfoTxtArea.setColumns(20);
        osInfoTxtArea.setRows(5);
        osInfoTxtArea.setAlignmentX(150.0F);

        javax.swing.GroupLayout osPanelLayout = new javax.swing.GroupLayout(osPanel);
        osPanel.setLayout(osPanelLayout);
        osPanelLayout.setHorizontalGroup(
            osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, osPanelLayout.createSequentialGroup()
                .addContainerGap(452, Short.MAX_VALUE)
                .addComponent(osInfoTxtArea, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(298, 298, 298))
        );
        osPanelLayout.setVerticalGroup(
            osPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(osPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(osInfoTxtArea, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        hardwareInfoPane.addTab("OS", osPanel);

        crAM.setText("© 2023 Adam Malone");

        madeWith.setText("Made using the OSHI library");

        oshiLink.setForeground(new java.awt.Color(0, 51, 204));
        oshiLink.setText("https://github.com/oshi/oshi");
        oshiLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        oshiLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                oshiLinkMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout aboutPanelLayout = new javax.swing.GroupLayout(aboutPanel);
        aboutPanel.setLayout(aboutPanelLayout);
        aboutPanelLayout.setHorizontalGroup(
            aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(crAM)
                    .addComponent(madeWith)
                    .addComponent(oshiLink))
                .addContainerGap(777, Short.MAX_VALUE))
        );
        aboutPanelLayout.setVerticalGroup(
            aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crAM)
                .addGap(18, 18, 18)
                .addComponent(madeWith)
                .addGap(18, 18, 18)
                .addComponent(oshiLink)
                .addContainerGap(202, Short.MAX_VALUE))
        );

        hardwareInfoPane.addTab("About", aboutPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hardwareInfoPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hardwareInfoPane)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cpuNameTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cpuNameTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cpuNameTxtFieldActionPerformed

    private void gpuNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gpuNameFieldActionPerformed
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        GraphicsCard[] graphicsCards = hal.getGraphicsCards().toArray(new GraphicsCard[0]);
        GraphicsCard selectedGpu = null;
        
        ImageIcon icon;

        String selectedGpuName = (String) gpuNameField.getSelectedItem();
        
        if(StringUtils.containsIgnoreCase(selectedGpuName, "NVIDIA")){
            icon = new ImageIcon("images/pngfind.com-nvidia-logo-png-797573_15");
        }

        for (GraphicsCard gpu : graphicsCards) {
            if (gpu.getName().equals(selectedGpuName)) {
                selectedGpu = gpu;
                break;
            }
        }
        if (selectedGpu != null) {
            gpuVendorField.setText(selectedGpu.getVendor().toString());
            double realVRAM = selectedGpu.getVRam() / 1073741824;
            gpuVRAMField.setText(String.valueOf(realVRAM) + " GB");
            String gpuVersionInfo = selectedGpu.getVersionInfo();
            gpuVersion.setText(gpuVersionInfo);

        }
    }//GEN-LAST:event_gpuNameFieldActionPerformed

    private void diskNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diskNameFieldActionPerformed
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        HWDiskStore selectedDisk = null;
        String selectedModel = (String) diskNameField.getSelectedItem();
        DecimalFormat df = new DecimalFormat("#.#");

        for (HWDiskStore disk : hal.getDiskStores()) {
            if (disk.getModel().equals(selectedModel)) {
                selectedDisk = disk;
                break;
            }
        }
        if (selectedDisk != null) {
            diskDriveModelField.setText(selectedDisk.getModel());
            double sizeInGB = selectedDisk.getSize() / (1024.0 * 1024.0 * 1024.0);
            String formattedSize = df.format(sizeInGB);
            diskDriveSizeField.setText(String.valueOf(formattedSize) + "GB");
            diskDriveSerialField.setText(selectedDisk.getSerial());
        }
    }//GEN-LAST:event_diskNameFieldActionPerformed

    private void hardwareInfoPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_hardwareInfoPaneStateChanged


    }//GEN-LAST:event_hardwareInfoPaneStateChanged

    private void networkNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_networkNameFieldActionPerformed
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        List<NetworkIF> networks = hal.getNetworkIFs();
        DecimalFormat df = new DecimalFormat("0.00");

        for (NetworkIF net : networks) {
            long speed = net.getSpeed();
            double speedMbps = speed / 1_000_000.0;
            String macAddress = net.getMacaddr();
            String ipv4 = Arrays.toString(net.getIPv4addr()).replaceAll("[\\[\\],]", "");
            String ipv6 = Arrays.toString(net.getIPv6addr()).replaceAll("[\\[\\],]", "");
            macNameField.setText(macAddress);
            ipv4NameField.setText(ipv4);
            ipv6NameField.setText(ipv6);
            netSpeedFieldName.setText(String.valueOf(df.format(speedMbps)));
        }
    }//GEN-LAST:event_networkNameFieldActionPerformed

    private void netActivityBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netActivityBtnActionPerformed
        JFrame graphFrame = new JFrame("Network Usage");
        graphFrame.setLocationRelativeTo(hardwareInfoPane);
        graphFrame.setSize(800, 600);
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        List<NetworkIF> network = hal.getNetworkIFs();

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries series = new TimeSeries("Network Activity");

        try {

            for (NetworkIF net : network) {
                long rx = net.getBytesRecv();
                long tx = net.getBytesSent();
                series.add(new Millisecond(), rx + tx);
                Thread.sleep(1000);
            }

            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createTimeSeriesChart("Network Activity", "Time", "Bytes per second", dataset, true, true, false);
            ChartPanel chartPanel = new ChartPanel(chart);
            graphFrame.getContentPane().add(chartPanel);
            graphFrame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_netActivityBtnActionPerformed

    private void gpuVendorFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gpuVendorFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gpuVendorFieldActionPerformed

    private void gpuVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gpuVersionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gpuVersionActionPerformed

    private void oshiLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_oshiLinkMouseClicked
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/oshi/oshi"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_oshiLinkMouseClicked
    // End of variables declaration

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HardwareDetectGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HardwareDetectGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HardwareDetectGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HardwareDetectGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HardwareDetectGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aboutPanel;
    private javax.swing.JLabel bankLbl;
    private javax.swing.JFormattedTextField bankTxtField;
    private javax.swing.JTextArea cacheField;
    private javax.swing.JScrollPane cacheScrlPne;
    private javax.swing.JLabel cpuNameLbl;
    private javax.swing.JFormattedTextField cpuNameTxtField;
    private javax.swing.JPanel cpuPanel;
    private javax.swing.JFormattedTextField cpuPercent;
    private javax.swing.JLabel cpuTempLbl;
    private javax.swing.JLabel cpuUsageLbl;
    private javax.swing.JLabel crAM;
    private javax.swing.JFormattedTextField diskDriveModelField;
    private javax.swing.JFormattedTextField diskDriveSerialField;
    private javax.swing.JFormattedTextField diskDriveSizeField;
    private javax.swing.JLabel diskLbl;
    private javax.swing.JComboBox<String> diskNameField;
    private javax.swing.JPanel diskPanel;
    private javax.swing.JLabel diskSerialLbl;
    private javax.swing.JLabel diskSizeLbl;
    private javax.swing.JComboBox<String> gpuNameField;
    private javax.swing.JLabel gpuNameLbl;
    private javax.swing.JPanel gpuPanel;
    private javax.swing.JFormattedTextField gpuVRAMField;
    private javax.swing.JFormattedTextField gpuVendorField;
    private javax.swing.JLabel gpuVendorLbl;
    private javax.swing.JFormattedTextField gpuVersion;
    private javax.swing.JLabel gpuVersionLbl;
    private javax.swing.JLabel gpuVramLbl;
    private javax.swing.JTabbedPane hardwareInfoPane;
    private javax.swing.JLabel ipv4Lbl;
    private javax.swing.JFormattedTextField ipv4NameField;
    private javax.swing.JLabel ipv6Lbl;
    private javax.swing.JFormattedTextField ipv6NameField;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JFormattedTextField logicalCores;
    private javax.swing.JLabel logicalLbl;
    private javax.swing.JLabel macAddressLbl;
    private javax.swing.JFormattedTextField macNameField;
    private javax.swing.JLabel madeWith;
    private javax.swing.JFormattedTextField memSpeed;
    private javax.swing.JLabel memSpeedLbl1;
    private javax.swing.JLabel memTypeLbl;
    private javax.swing.JLabel memTypeLbl1;
    private javax.swing.JLabel memUsageLbl;
    private javax.swing.JFormattedTextField memoryManufacturer;
    private javax.swing.JPanel memoryPanel;
    private javax.swing.JFormattedTextField memoryPercent;
    private javax.swing.JFormattedTextField memorySize;
    private javax.swing.JLabel memorySizeLbl;
    private javax.swing.JFormattedTextField memoryType;
    private javax.swing.JLabel modeLbl;
    private javax.swing.JButton netActivityBtn;
    private javax.swing.JFormattedTextField netSpeedFieldName;
    private javax.swing.JLabel netSpeedLbl;
    private javax.swing.JComboBox<String> networkNameField;
    private javax.swing.JLabel networkNameLbl;
    private javax.swing.JPanel networkPanel;
    private javax.swing.JTextArea osInfoTxtArea;
    private javax.swing.JPanel osPanel;
    private javax.swing.JLabel oshiLink;
    // End of variables declaration//GEN-END:variables
}
