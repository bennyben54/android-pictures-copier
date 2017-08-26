/**
 * 
 */
package com.beo.app.android.pictures.copier.process;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

/**
 * 26 août 2017
 * @author bennyben54
 */
public class AndroidDeviceAccessor {

	private static final Logger log = Logger.getLogger(AndroidDeviceAccessor.class.getName());

	@NonNull
	public List<Path> getAndroidDevices() {
		List<Path> devices = new ArrayList<>();
		try {
			Files.list(Paths.get("/run/user")).filter(p -> {
				try {
					return Files.list(p).anyMatch(q -> q.getFileName().toString().contains("gvfs"));
				} catch (IOException e) {
					log.log(Level.WARNING, p.getFileName().toString(), e);
					return false;
				}
			}).forEach(devices::add);
		} catch (IOException e) {
			log.log(Level.WARNING, "Erreur", e);
		}
		return devices;
	}

	@NonNull
	public List<Path> getPictures(@NonNull final Path folder, final Date from, final Date to) {
		List<Path> devices = new ArrayList<>();

		final String fromDate = extractBasicIsoDate(from);
		final String toDate = extractBasicIsoDate(to);

		try {
			Files.list(folder).filter(p -> {
				try {
					return Files.list(p).anyMatch(new FromToPredicate(fromDate, toDate));
				} catch (IOException e) {
					log.log(Level.WARNING, p.getFileName().toString(), e);
					return false;
				}
			}).forEach(devices::add);
		} catch (IOException e) {
			log.log(Level.WARNING, "Erreur", e);
		}
		return devices;
	}

	private class FromToPredicate implements Predicate<Path> {

		final String fromDate;
		final String toDate;

		public FromToPredicate(final String fromDate, final String toDate) {
			super();
			this.fromDate = fromDate;
			this.toDate = toDate;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.function.Predicate#test(java.lang.Object)
		 */
		@Override
		public boolean test(final Path t) {

			boolean after = true;
			boolean before = true;

			if (fromDate != null && !"".equals(fromDate)) {
				after = t.getFileName().toString().compareTo(fromDate) >= 0;
			}

			if (toDate != null && !"".equals(toDate)) {
				after = t.getFileName().toString().compareTo(toDate) <= 0;
			}
			return after && before;
		}

	}

	/**
	 * @param from
	 * @param fromDate
	 * @return
	 */
	private String extractBasicIsoDate(final Date from) {
		if (from != null) {
			LocalDate date = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			return date.format(DateTimeFormatter.BASIC_ISO_DATE);
		}
		return null;
	}

}
