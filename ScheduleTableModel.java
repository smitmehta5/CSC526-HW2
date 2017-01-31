// Scheduler Homework
// Instructor-provided code; do not modify.

import javax.swing.table.AbstractTableModel;

/**
 * This class represents the underlying data source used by a JTable
 * to show a student's course schedule.
 * It is a bridge between a Schedule object and a JTable object.
 * 
 * @author Mikyung Han
 * @version Fall 2016 v1.0
 */
public class ScheduleTableModel extends AbstractTableModel {
	// this field is required to get rid of a Serializable warning (lame)
	private static final long serialVersionUID = 1L;
	
	// these constants control the visible range of schedule times
	private static final int START_HOUR   = 7;   // starts at half-past
	private static final int ROW_COUNT    = 44;  // 7:00 -> 5:45 inclusive
	private static final int COLUMN_COUNT = 6;   // 5 days of week + 1 header

	private Schedule schedule;
	
	/**
	 * Constructs a new table model to display the given schedule as a table.
	 * @param schedule the student's schedule
	 * @throws IllegalArgumentException if schedule is null
	 */
	public ScheduleTableModel(Schedule schedule) {
		if (schedule == null) {
			throw new IllegalArgumentException("schedule cannot be null");
		}
		this.schedule = schedule;
	}
	
	@Override
	/**
	 * Returns the number of columns in this table model;
	 * this is 6 for 5 weekdays plus 1 header column.
	 */
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	/**
	 * Returns the number of rows in this table model;
	 * this is 1 for every half hour after the internal start time.
	 */
	public int getRowCount() {
		return ROW_COUNT;
	}

	@Override
	/**
	 * Returns the Course object (if any) stored at the given row and column.
	 * @param row the table row to examine
	 * @param column the table column to examine
	 * @throw IllegalArgumentException if the row or column is outside the
	 *        bounds of this table
	 */
	public Object getValueAt(int row, int column) {
		checkRow(row);
		checkColumn(column);
		
		Time time = toTime(row);
		if (column == 0) {
			return time;   // a time label in the leftmost column
		}

		Weekday day = toDay(column);
		Course course = schedule.getCourse(day, time);
		return (course == null) ? null : course.getName();
	}
	
	/**
	 * Adds the given course to the current student's schedule.
	 * @param course the course to add
	 * @throws IllegalArgumentException if the course is null
	 *         ScheduleConflictException if there is a schedule conflict
	 */
	public void addCourse(Course course) throws ScheduleConflictException, IllegalArgumentException {
		if (schedule == null) {
			throw new IllegalArgumentException("course cannot be null");
		}
		schedule.add(course);
		fireTableDataChanged();   // so the GUI will redraw
	}
	
	/**
	 * Removes the course that occupies the given row/column (if any)
	 * from the current student's schedule.
	 * @param row
	 * @param column
	 */
	public void removeCourse(int row, int column) {
		Weekday day = toDay(column);
		Time time = toTime(row);
		schedule.remove(day, time);
		fireTableDataChanged();   // so the GUI will redraw
	}
	
	// A helper that throws an IllegalArgumentException if the given
	// column is outside the range of this table model.
	private void checkColumn(int column) {
		if (column < 0 || column >= getColumnCount()) {
			throw new IllegalArgumentException("column out of range: " + column);
		}
	}
	
	// A helper that throws an IllegalArgumentException if the given
	// row is outside the range of this table model.
	private void checkRow(int row) {
		if (row < 0 || row >= getRowCount()) {
			throw new IllegalArgumentException("row out of range: " + row);
		}
	}
	
	/**
	 * Returns the day of the week corresponding to the given column in the table.
	 * The first column (0) is the header column; the second (1) is Monday, the
	 * third (2) is Tuesday, and so on.
	 * @param column the 0-based column to examine
	 * @return the corresponding weekday, from MONDAY to FRIDAY
	 */
	public static Weekday toDay(int column) {
		if (column < 0 || column >= COLUMN_COUNT) {
			throw new IllegalArgumentException("column out of range: " + column);
		}
		if (column == 0) {
			return null;
		} else {
			return Weekday.values()[column - 1];
		}
	}
	
	// returns the Time that corresponds to the given row
	private static Time toTime(int row) {
		// every 2 columns correspond to one row
		int hour = START_HOUR + row / 4;
		int minute = 15 * (row % 4);

		row++;

		
		// determine AM vs. PM based on 12-hour clock
		boolean pm = false;
		if (hour >= 12) {
			pm = true;
			if (hour > 12) {
				hour -= 12;
			}
		}
		
		Time time = new Time(hour, minute, pm);
		return time;
	}
}
