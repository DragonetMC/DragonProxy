/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;
import jline.console.ConsoleReader;

import org.dragonet.proxy.utilities.Terminal;

public class ConsoleManager {

    private static final String CONSOLE_DATE = "HH:mm:ss";
    private static final String FILE_DATE = "yyyy/MM/dd HH:mm:ss";
    private static final Logger logger = Logger.getLogger("");

    private final DragonProxy proxy;

    private ConsoleReader reader;

    public ConsoleManager(DragonProxy proxy) {
        this.proxy = proxy;

        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }

        logger.addHandler(new FancyConsoleHandler());

        //Reader must be initialized before standard streams are changed
        try {
            reader = new ConsoleReader();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception initializing console reader", ex);
        }

        System.setOut(new PrintStream(new LoggerOutputStream(Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(Level.WARNING), true));
    }

    public void startConsole() {
        Thread thread = new ConsoleCommandThread();
        thread.setName("ConsoleCommandThread");
        thread.setDaemon(true);
        thread.start();
    }

    public void startFile(String logfile) {
        File parent = new File(logfile).getParentFile();
        if (parent != null) {
            if (!parent.isDirectory() && !parent.mkdirs()) {
                logger.warning("Could not create log folder: " + parent);
            }
        }
        Handler fileHandler = new RotatingFileHandler(logfile);
        fileHandler.setFormatter(new DateOutputFormatter(FILE_DATE, false));
        logger.addHandler(fileHandler);
    }

    private class ConsoleCommandThread extends Thread {

        @Override
        public void run() {
            String command = "";
            while (!proxy.isShuttingDown()) {
                try {
                    command = reader.readLine(">", null);

                    if (command == null || command.trim().length() == 0) {
                        continue;
                    }

                    proxy.getCommandRegister().callCommand(command);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Error while reading commands", ex);
                }
            }
        }
    }

    private static class LoggerOutputStream extends ByteArrayOutputStream {

        private final String separator = System.getProperty("line.separator");
        private final Level level;

        public LoggerOutputStream(Level level) {
            super();
            this.level = level;
        }

        @Override
        public synchronized void flush() throws IOException {
            super.flush();
            String record = this.toString();
            super.reset();

            if (record.length() > 0 && !record.equals(separator)) {
                logger.logp(level, "LoggerOutputStream", "log" + level, record);
            }
        }
    }

    private class FancyConsoleHandler extends ConsoleHandler {

        public FancyConsoleHandler() {
            setFormatter(new DateOutputFormatter(CONSOLE_DATE, true));
            setOutputStream(System.out);
        }

        @Override
        public synchronized void flush() {
            try {
                reader.print(ConsoleReader.RESET_LINE + "");
                reader.flush();
                super.flush();
                try {
                    reader.drawLine();
                } catch (Throwable ex) {
                    reader.getCursorBuffer().clear();
                }
                reader.flush();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "I/O exception flushing console output", ex);
            }
        }
    }

    private static class RotatingFileHandler extends StreamHandler {

        private final SimpleDateFormat dateFormat;
        private final String template;
        private final boolean rotate;
        private String filename;

        public RotatingFileHandler(String template) {
            this.template = template;
            rotate = template.contains("%D");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            filename = calculateFilename();
            updateOutput();
        }

        private void updateOutput() {
            try {
                setOutputStream(new FileOutputStream(filename, true));
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Unable to open " + filename + " for writing", ex);
            }
        }

        private void checkRotate() {
            if (rotate) {
                String newFilename = calculateFilename();
                if (!filename.equals(newFilename)) {
                    filename = newFilename;
                    // note that the console handler doesn't see this message
                    super.publish(new LogRecord(Level.INFO, "Log rotating to: " + filename));
                    updateOutput();
                }
            }
        }

        private String calculateFilename() {
            return template.replace("%D", dateFormat.format(new Date()));
        }

        @Override
        public synchronized void publish(LogRecord record) {
            if (!isLoggable(record)) {
                return;
            }
            checkRotate();
            super.publish(record);
            super.flush();
        }

        @Override
        public synchronized void flush() {
            checkRotate();
            super.flush();
        }
    }

    private class DateOutputFormatter extends Formatter {

        private final SimpleDateFormat date;
        private final boolean color;

        public DateOutputFormatter(String pattern, boolean color) {
            this.date = new SimpleDateFormat(pattern);
            this.color = color;
        }

        @Override
        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();

	    builder.append(Terminal.CYAN + "[");
            builder.append(date.format(record.getMillis()));
	    builder.append("]" + Terminal.WHITE);
            builder.append(" [");
            builder.append(record.getLevel().getLocalizedName().toUpperCase());
            builder.append("] ");
            builder.append(formatMessage(record) + Terminal.WHITE);
            builder.append('\n');

            if (record.getThrown() != null) {
                // StringWriter's close() is trivial
                @SuppressWarnings("resource")
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                builder.append(writer.toString());
            }

            return builder.toString();
        }
    }

}
