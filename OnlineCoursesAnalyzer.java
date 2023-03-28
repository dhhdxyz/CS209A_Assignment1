import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class OnlineCoursesAnalyzer {

  List<Course> courses = new ArrayList<>();

  public OnlineCoursesAnalyzer(String datasetPath) {
    BufferedReader br = null;
    String line;
    try {
      br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
      br.readLine();
      while ((line = br.readLine()) != null) {
        String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
        Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4], info[5],
                Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]),
               Integer.parseInt(info[9]), Integer.parseInt(info[10]), Double.parseDouble(info[11]),
            Double.parseDouble(info[12]), Double.parseDouble(info[13]),
                Double.parseDouble(info[14]), Double.parseDouble(info[15]),
                Double.parseDouble(info[16]), Double.parseDouble(info[17]),
            Double.parseDouble(info[18]), Double.parseDouble(info[19]),
                Double.parseDouble(info[20]), Double.parseDouble(info[21]),
                Double.parseDouble(info[22]));
        courses.add(course);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public Map<String, Integer> getPtcpCountByInst() {
    Map<String, Integer> cnt = courses.stream()
                .collect(Collectors.groupingBy(
                        Course::getInstitution,
                        Collectors.summingInt(Course::getParticipants)));
    return cnt;
  }

  public Map<String, Integer> getPtcpCountByInstAndSubject() {
    Map<String, Integer> cnt = courses.stream()
            .collect(Collectors.groupingBy(
                    course -> course.getInstitution() + "-" + course.getSubject(),
                    Collectors.summingInt(Course::getParticipants)));

    Map<String, Integer> sortedMap = cnt.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    return sortedMap;
  }

  public Map<String, List<List<String>>> getCourseListOfInstructor() {
    Map<String, List<List<String>>> list = new LinkedHashMap<>();

    List<String> name = courses.stream()
              .flatMap(course -> Arrays.stream(course.getInstructors().split(",")))
              .map(String::trim)
              .distinct()
              .collect(Collectors.toList());

    for (String n : name) {
      List<String> idc = courses.stream()
                .filter(course -> n.equals(course.getInstructors()))
                .map(Course::getTitle).sorted().distinct().collect(Collectors.toList());

      List<String> cdc = courses.stream()
                .filter(course -> {
                  List<String> ins = Arrays.asList(course.getInstructors().split(", "));
                  if (ins.size() > 1 && ins.contains(n)) {
                    return true;
                  } else {
                    return false;
                  }
                })
                .map(Course::getTitle).sorted().distinct().collect(Collectors.toList());

      list.put(n, Arrays.asList(idc, cdc));
    }
    return list;
  }

    public List<String> getCourses(int topK, String by) {
        return null;
    }

    public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
        return null;
    }

    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        return null;
    }

}

class Course {
    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;

    public Course(String institution, String number, Date launchDate,
                  String title, String instructors, String subject,
                  int year, int honorCode, int participants,
                  int audited, int certified, double percentAudited,
                  double percentCertified, double percentCertified50,
                  double percentVideo, double percentForum, double gradeHigherZero,
                  double totalHours, double medianHoursCertification,
                  double medianAge, double percentMale, double percentFemale,
                  double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) title = title.substring(1);
        if (title.endsWith("\"")) title = title.substring(0, title.length() - 1);
        this.title = title;
        if (instructors.startsWith("\"")) instructors = instructors.substring(1);
        if (instructors.endsWith("\"")) instructors = instructors.substring(0, instructors.length() - 1);
        this.instructors = instructors;
        if (subject.startsWith("\"")) subject = subject.substring(1);
        if (subject.endsWith("\"")) subject = subject.substring(0, subject.length() - 1);
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }

    public String getInstitution() {
        return institution;
    }

    public String getNumber() {
        return number;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructors() {
        return instructors;
    }

    public String getSubject() {
        return subject;
    }

    public int getYear() {
        return year;
    }

    public int getHonorCode() {
        return honorCode;
    }

    public int getParticipants() {
        return participants;
    }

    public int getAudited() {
        return audited;
    }

    public int getCertified() {
        return certified;
    }

    public double getPercentAudited() {
        return percentAudited;
    }

    public double getPercentCertified() {
        return percentCertified;
    }

    public double getPercentCertified50() {
        return percentCertified50;
    }

    public double getPercentVideo() {
        return percentVideo;
    }

    public double getPercentForum() {
        return percentForum;
    }

    public double getGradeHigherZero() {
        return gradeHigherZero;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public double getMedianHoursCertification() {
        return medianHoursCertification;
    }

    public double getMedianAge() {
        return medianAge;
    }

    public double getPercentMale() {
        return percentMale;
    }

    public double getPercentFemale() {
        return percentFemale;
    }

    public double getPercentDegree() {
        return percentDegree;
    }
}