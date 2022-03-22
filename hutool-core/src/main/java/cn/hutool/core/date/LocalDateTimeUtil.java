package cn.hutool.core.date;

import cn.hutool.core.date.format.GlobalCustomFormat;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.TimeZone;

/**
 * JDK8+中的{@link LocalDateTime} 工具类封装
 *
 * @author looly
 * @see DateUtil java7和一下版本，使用Date工具类
 * @see DatePattern 常用格式工具类
 * @since 5.3.9
 */
public class LocalDateTimeUtil {

	/**
	 * 当前时间，默认时区
	 *
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}，使用默认时区
	 *
	 * @param instant {@link Instant}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Instant instant) {
		return of(instant, ZoneId.systemDefault());
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}，使用UTC时区
	 *
	 * @param instant {@link Instant}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime ofUTC(Instant instant) {
		return of(instant, ZoneId.of("UTC"));
	}

	/**
	 * {@link ZonedDateTime}转{@link LocalDateTime}
	 *
	 * @param zonedDateTime {@link ZonedDateTime}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(ZonedDateTime zonedDateTime) {
		if (null == zonedDateTime) {
			return null;
		}
		return zonedDateTime.toLocalDateTime();
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}
	 *
	 * @param instant {@link Instant}
	 * @param zoneId  时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Instant instant, ZoneId zoneId) {
		if (null == instant) {
			return null;
		}

		return LocalDateTime.ofInstant(instant, ObjectUtil.defaultIfNull(zoneId, ZoneId::systemDefault));
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}
	 *
	 * @param instant  {@link Instant}
	 * @param timeZone 时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Instant instant, TimeZone timeZone) {
		if (null == instant) {
			return null;
		}

		return of(instant, ObjectUtil.defaultIfNull(timeZone, TimeZone::getDefault).toZoneId());
	}

	/**
	 * 毫秒转{@link LocalDateTime}，使用默认时区
	 *
	 * <p>注意：此方法使用默认时区，如果非UTC，会产生时间偏移</p>
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(long epochMilli) {
		return of(Instant.ofEpochMilli(epochMilli));
	}

	/**
	 * 毫秒转{@link LocalDateTime}，使用UTC时区
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime ofUTC(long epochMilli) {
		return ofUTC(Instant.ofEpochMilli(epochMilli));
	}

	/**
	 * 毫秒转{@link LocalDateTime}，根据时区不同，结果会产生时间偏移
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @param zoneId     时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(long epochMilli, ZoneId zoneId) {
		return of(Instant.ofEpochMilli(epochMilli), zoneId);
	}

	/**
	 * 毫秒转{@link LocalDateTime}，结果会产生时间偏移
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @param timeZone   时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(long epochMilli, TimeZone timeZone) {
		return of(Instant.ofEpochMilli(epochMilli), timeZone);
	}

	/**
	 * {@link Date}转{@link LocalDateTime}，使用默认时区
	 *
	 * @param date Date对象
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Date date) {
		if (null == date) {
			return null;
		}

		if (date instanceof DateTime) {
			return of(date.toInstant(), ((DateTime) date).getZoneId());
		}
		return of(date.toInstant());
	}

	/**
	 * {@link TemporalAccessor}转{@link LocalDateTime}，使用默认时区
	 *
	 * @param temporalAccessor {@link TemporalAccessor}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(TemporalAccessor temporalAccessor) {
		if (null == temporalAccessor) {
			return null;
		}

		if (temporalAccessor instanceof LocalDate) {
			return ((LocalDate) temporalAccessor).atStartOfDay();
		} else if(temporalAccessor instanceof Instant){
			return LocalDateTime.ofInstant((Instant) temporalAccessor, ZoneId.systemDefault());
		}

		return LocalDateTime.of(
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.MONTH_OF_YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.DAY_OF_MONTH),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.HOUR_OF_DAY),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.MINUTE_OF_HOUR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.SECOND_OF_MINUTE),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.NANO_OF_SECOND)
		);
	}

	/**
	 * {@link TemporalAccessor}转{@link LocalDate}，使用默认时区
	 *
	 * @param temporalAccessor {@link TemporalAccessor}
	 * @return {@link LocalDate}
	 * @since 5.3.10
	 */
	public static LocalDate ofDate(TemporalAccessor temporalAccessor) {
		if (null == temporalAccessor) {
			return null;
		}

		if (temporalAccessor instanceof LocalDateTime) {
			return ((LocalDateTime) temporalAccessor).toLocalDate();
		} else if(temporalAccessor instanceof Instant){
			return of(temporalAccessor).toLocalDate();
		}

		return LocalDate.of(
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.MONTH_OF_YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.DAY_OF_MONTH)
		);
	}

	/**
	 * 解析日期时间字符串为{@link LocalDateTime}，仅支持yyyy-MM-dd'T'HH:mm:ss格式，例如：2007-12-03T10:15:30<br>
	 * 即{@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}
	 *
	 * @param text 日期时间字符串
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime parse(CharSequence text) {
		return parse(text, (DateTimeFormatter) null);
	}

	/**
	 * 解析日期时间字符串为{@link LocalDateTime}，格式支持日期时间、日期、时间<br>
	 * 如果formatter为{code null}，则使用{@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}
	 *
	 * @param text      日期时间字符串
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime parse(CharSequence text, DateTimeFormatter formatter) {
		if (null == text) {
			return null;
		}
		if (null == formatter) {
			return LocalDateTime.parse(text);
		}

		return of(formatter.parse(text));
	}

	/**
	 * 解析日期时间字符串为{@link LocalDateTime}
	 *
	 * @param text   日期时间字符串
	 * @param format 日期格式，类似于yyyy-MM-dd HH:mm:ss,SSS
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime parse(CharSequence text, String format) {
		if (null == text) {
			return null;
		}

		if (GlobalCustomFormat.isCustomFormat(format)) {
			return of(GlobalCustomFormat.parse(text, format));
		}

		DateTimeFormatter formatter = null;
		if (StrUtil.isNotBlank(format)) {
			// 修复yyyyMMddHHmmssSSS格式不能解析的问题
			// fix issue#1082
			//see https://stackoverflow.com/questions/22588051/is-java-time-failing-to-parse-fraction-of-second
			// jdk8 bug at: https://bugs.openjdk.java.net/browse/JDK-8031085
			if (StrUtil.startWithIgnoreEquals(format, DatePattern.PURE_DATETIME_PATTERN)) {
				final String fraction = StrUtil.removePrefix(format, DatePattern.PURE_DATETIME_PATTERN);
				if (ReUtil.isMatch("[S]{1,2}", fraction)) {
					//将yyyyMMddHHmmssS、yyyyMMddHHmmssSS的日期统一替换为yyyyMMddHHmmssSSS格式，用0补
					text += StrUtil.repeat('0', 3 - fraction.length());
				}
				formatter = new DateTimeFormatterBuilder()
						.appendPattern(DatePattern.PURE_DATETIME_PATTERN)
						.appendValue(ChronoField.MILLI_OF_SECOND, 3)
						.toFormatter();
			} else {
				formatter = DateTimeFormatter.ofPattern(format);
			}
		}

		return parse(text, formatter);
	}

	/**
	 * 解析日期时间字符串为{@link LocalDate}，仅支持yyyy-MM-dd'T'HH:mm:ss格式，例如：2007-12-03T10:15:30
	 *
	 * @param text 日期时间字符串
	 * @return {@link LocalDate}
	 * @since 5.3.10
	 */
	public static LocalDate parseDate(CharSequence text) {
		return parseDate(text, (DateTimeFormatter) null);
	}

	/**
	 * 解析日期时间字符串为{@link LocalDate}，格式支持日期
	 *
	 * @param text      日期时间字符串
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return {@link LocalDate}
	 * @since 5.3.10
	 */
	public static LocalDate parseDate(CharSequence text, DateTimeFormatter formatter) {
		if (null == text) {
			return null;
		}
		if (null == formatter) {
			return LocalDate.parse(text);
		}

		return ofDate(formatter.parse(text));
	}

	/**
	 * 解析日期字符串为{@link LocalDate}
	 *
	 * @param text   日期字符串
	 * @param format 日期格式，类似于yyyy-MM-dd
	 * @return {@link LocalDateTime}
	 */
	public static LocalDate parseDate(CharSequence text, String format) {
		if (null == text) {
			return null;
		}
		return parseDate(text, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 格式化日期时间为yyyy-MM-dd HH:mm:ss格式
	 *
	 * @param time {@link LocalDateTime}
	 * @return 格式化后的字符串
	 * @since 5.3.11
	 */
	public static String formatNormal(LocalDateTime time) {
		return format(time, DatePattern.NORM_DATETIME_FORMATTER);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param time      {@link LocalDateTime}
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return 格式化后的字符串
	 */
	public static String format(LocalDateTime time, DateTimeFormatter formatter) {
		return TemporalAccessorUtil.format(time, formatter);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param time   {@link LocalDateTime}
	 * @param format 日期格式，类似于yyyy-MM-dd HH:mm:ss,SSS
	 * @return 格式化后的字符串
	 */
	public static String format(LocalDateTime time, String format) {
		return TemporalAccessorUtil.format(time, format);
	}

	/**
	 * 格式化日期时间为yyyy-MM-dd格式
	 *
	 * @param date {@link LocalDate}
	 * @return 格式化后的字符串
	 * @since 5.3.11
	 */
	public static String formatNormal(LocalDate date) {
		return format(date, DatePattern.NORM_DATE_FORMATTER);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param date      {@link LocalDate}
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}; 常量如： {@link DatePattern#NORM_DATE_FORMATTER}, {@link DatePattern#NORM_DATETIME_FORMATTER}
	 * @return 格式化后的字符串
	 * @since 5.3.10
	 */
	public static String format(LocalDate date, DateTimeFormatter formatter) {
		return TemporalAccessorUtil.format(date, formatter);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param date   {@link LocalDate}
	 * @param format 日期格式，类似于yyyy-MM-dd, 常量如 {@link DatePattern#NORM_DATE_PATTERN}, {@link DatePattern#NORM_DATETIME_PATTERN}
	 * @return 格式化后的字符串
	 * @since 5.3.10
	 */
	public static String format(LocalDate date, String format) {
		if (null == date) {
			return null;
		}
		return format(date, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 日期偏移,根据field不同加不同值（偏移会修改传入的对象）
	 *
	 * @param time   {@link LocalDateTime}
	 * @param number 偏移量，正数为向后偏移，负数为向前偏移
	 * @param field  偏移单位，见{@link ChronoUnit}，不能为null
	 * @return 偏移后的日期时间
	 */
	public static LocalDateTime offset(LocalDateTime time, long number, TemporalUnit field) {
		if (null == time) {
			return null;
		}

		return time.plus(number, field);
	}

	/**
	 * 获取两个日期的差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 返回结果为{@link Duration}对象，通过调用toXXX方法返回相差单位
	 *
	 * @param startTimeInclude 开始时间（包含）
	 * @param endTimeExclude   结束时间（不包含）
	 * @return 时间差 {@link Duration}对象
	 * @see TemporalUtil#between(Temporal, Temporal)
	 */
	public static Duration between(LocalDateTime startTimeInclude, LocalDateTime endTimeExclude) {
		return TemporalUtil.between(startTimeInclude, endTimeExclude);
	}

	/**
	 * 获取两个日期的差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 返回结果为时间差的long值
	 *
	 * @param startTimeInclude 开始时间（包括）
	 * @param endTimeExclude   结束时间（不包括）
	 * @param unit             时间差单位
	 * @return 时间差
	 * @since 5.4.5
	 */
	public static long between(LocalDateTime startTimeInclude, LocalDateTime endTimeExclude, ChronoUnit unit) {
		return TemporalUtil.between(startTimeInclude, endTimeExclude, unit);
	}

	/**
	 * 获取两个日期的表象时间差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 比如2011年2月1日，和2021年8月11日，日相差了10天，月相差6月
	 *
	 * @param startTimeInclude 开始时间（包括）
	 * @param endTimeExclude   结束时间（不包括）
	 * @return 时间差
	 * @since 5.4.5
	 */
	public static Period betweenPeriod(LocalDate startTimeInclude, LocalDate endTimeExclude) {
		return Period.between(startTimeInclude, endTimeExclude);
	}

	/**
	 * 修改为一天的开始时间，例如：2020-02-02 00:00:00,000
	 *
	 * @param time 日期时间
	 * @return 一天的开始时间
	 */
	public static LocalDateTime beginOfDay(LocalDateTime time) {
		return time.with(LocalTime.MIN);
	}

	/**
	 * 修改为一天的结束时间，例如：2020-02-02 23:59:59,999
	 *
	 * @param time 日期时间
	 * @return 一天的结束时间
	 */
	public static LocalDateTime endOfDay(LocalDateTime time) {
		return endOfDay(time, false);
	}

	/**
	 * 修改为一天的结束时间，例如：
	 * <ul>
	 * 	<li>毫秒不归零：2020-02-02 23:59:59,999</li>
	 * 	<li>毫秒归零：2020-02-02 23:59:59,000</li>
	 * </ul>
	 *
	 * @param time                日期时间
	 * @param truncateMillisecond 是否毫秒归零
	 * @return 一天的结束时间
	 * @since 5.7.18
	 */
	public static LocalDateTime endOfDay(LocalDateTime time, boolean truncateMillisecond) {
		if (truncateMillisecond) {
			return time.with(LocalTime.of(23, 59, 59));
		}
		return time.with(LocalTime.MAX);
	}

	/**
	 * {@link TemporalAccessor}转换为 时间戳（从1970-01-01T00:00:00Z开始的毫秒数）
	 *
	 * @param temporalAccessor Date对象
	 * @return {@link Instant}对象
	 * @see TemporalAccessorUtil#toEpochMilli(TemporalAccessor)
	 * @since 5.4.1
	 */
	public static long toEpochMilli(TemporalAccessor temporalAccessor) {
		return TemporalAccessorUtil.toEpochMilli(temporalAccessor);
	}

	/**
	 * 是否为周末（周六或周日）
	 *
	 * @param localDateTime 判定的日期{@link LocalDateTime}
	 * @return 是否为周末（周六或周日）
	 * @since 5.7.6
	 */
	public static boolean isWeekend(LocalDateTime localDateTime) {
		return isWeekend(localDateTime.toLocalDate());
	}

	/**
	 * 是否为周末（周六或周日）
	 *
	 * @param localDate 判定的日期{@link LocalDate}
	 * @return 是否为周末（周六或周日）
	 * @since 5.7.6
	 */
	public static boolean isWeekend(LocalDate localDate) {
		final DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		return DayOfWeek.SATURDAY == dayOfWeek || DayOfWeek.SUNDAY == dayOfWeek;
	}

	/**
	 * 获取{@link LocalDate}对应的星期值
	 *
	 * @param localDate 日期{@link LocalDate}
	 * @return {@link Week}
	 * @since 5.7.14
	 */
	public static Week dayOfWeek(LocalDate localDate) {
		return Week.of(localDate.getDayOfWeek());
	}

	/**
	 * 检查两个时间段是否有时间重叠<br>
	 * 重叠指两个时间段是否有交集
	 *
	 * @param realStartTime 第一个时间段的开始时间
	 * @param realEndTime   第一个时间段的结束时间
	 * @param startTime     第二个时间段的开始时间
	 * @param endTime       第二个时间段的结束时间
	 * @return true 表示时间有重合
	 * @since 5.7.20
	 */
	public static boolean isOverlap(ChronoLocalDateTime<?> realStartTime, ChronoLocalDateTime<?> realEndTime,
									ChronoLocalDateTime<?> startTime, ChronoLocalDateTime<?> endTime) {

		// x>b||a>y 无交集
		// 则有交集的逻辑为 !(x>b||a>y)
		// 根据德摩根公式，可化简为 x<=b && a<=y
		return startTime.isBefore(realEndTime) && endTime.isAfter(realStartTime);
	}

	/**
	 * 获得指定日期是所在年份的第几周，如：
	 * <ul>
	 *     <li>如果一年的第一天是星期一，则第一周从第一天开始，没有零周</li>
	 *     <li>如果一年的第二天是星期一，则第一周从第二天开始，而第一天在零周</li>
	 *     <li>如果一年中的第4天是星期一，则第1周从第4周开始，第1至第3周在零周开始</li>
	 *     <li>如果一年中的第5天是星期一，则第二周从第5周开始，第1至第4周在第1周</li>
	 * </ul>
	 *
	 *
	 * @param date 日期（{@link LocalDate} 或者 {@link LocalDateTime}等）
	 * @return 所在年的第几周
	 * @since 5.7.21
	 */
	public static int weekOfYear(TemporalAccessor date){
		return TemporalAccessorUtil.get(date, WeekFields.ISO.weekOfYear());
	}
}
