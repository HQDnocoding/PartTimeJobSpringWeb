package com.myweb.repositories.impl;

import com.myweb.pojo.Day;
import com.myweb.pojo.DayJob;
import com.myweb.pojo.Job;
import com.myweb.repositories.JobRepository;
import com.myweb.utils.GeneralUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
@Transactional
public class JobRepositoryImplement implements JobRepository {

    private static final Logger logger = Logger.getLogger(JobRepositoryImplement.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Map<String, Object> searchJobs(Map<String, String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Job> countRoot = countQuery.from(Job.class);
        countRoot.join("companyId", JoinType.LEFT);
        List<Predicate> countPredicates = buildPredicates(params, cb, countRoot);
        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        Long totalRecords = session.createQuery(countQuery).getSingleResult();

        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);
        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(params, cb, jobRoot);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(jobRoot.get("id")));

        int page = 1;
        if (params != null) {
            try {
                page = Integer.parseInt(params.getOrDefault("page", GeneralUtils.PAGE));
            } catch (NumberFormatException e) {
                logger.warning("Invalid page format: " + params.get("page"));
            }
        }
        int start = (page - 1) * GeneralUtils.PAGE_SIZE;
        List<Job> jobs = session.createQuery(cq)
                .setFirstResult(start)
                .setMaxResults(GeneralUtils.PAGE_SIZE)
                .getResultList();

        for (Job job : jobs) {
            Hibernate.initialize(job.getMajorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        logger.info("Total records: " + totalRecords);
        logger.info("Page: " + page + ", Start: " + start + ", Page size: " + GeneralUtils.PAGE_SIZE);
        logger.info("Jobs retrieved: " + jobs.size());
        jobs.forEach(job -> logger.info("Job ID: " + job.getId()));

        int totalPages = (int) Math.ceil((double) totalRecords / GeneralUtils.PAGE_SIZE);

        Map<String, Object> result = new HashMap<>();
        result.put("jobs", jobs);
        result.put("currentPage", page);
        result.put("pageSize", GeneralUtils.PAGE_SIZE);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalRecords);

        return result;
    }

    private List<Predicate> buildPredicates(Map<String, String> params, CriteriaBuilder cb, Root<Job> jobRoot) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));

        if (params != null) {
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.or(
                        cb.like(cb.lower(jobRoot.get("jobName")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(jobRoot.get("description")), "%" + keyword.toLowerCase() + "%")
                ));
            }

            String majorId = params.get("majorId");
            if (majorId != null && !majorId.isEmpty()) {
                try {
                    predicates.add(cb.equal(jobRoot.join("majorJobCollection").join("majorId").get("id"), Integer.parseInt(majorId)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid majorId format: " + majorId);
                }
            }

            String salaryMin = params.get("salaryMin");
            if (salaryMin != null && !salaryMin.isEmpty()) {
                try {
                    predicates.add(cb.greaterThanOrEqualTo(jobRoot.get("salaryMin"), new BigInteger(salaryMin)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid salaryMin format: " + salaryMin);
                }
            }
            String salaryMax = params.get("salaryMax");
            if (salaryMax != null && !salaryMax.isEmpty()) {
                try {
                    predicates.add(cb.lessThanOrEqualTo(jobRoot.get("salaryMax"), new BigInteger(salaryMax)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid salaryMax format: " + salaryMax);
                }
            }

            String city = params.get("city");
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(jobRoot.get("city"), city));
            }
            String district = params.get("district");
            if (district != null && !district.isEmpty()) {
                predicates.add(cb.equal(jobRoot.get("district"), district));
            }

            String dayId = params.get("dayId");
            if (dayId != null && !dayId.isEmpty()) {
                try {
                    predicates.add(cb.equal(jobRoot.join("dayJobCollection").join("dayId").get("id"), Integer.parseInt(dayId)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid dayId format: " + dayId);
                }
            }
        }

        return predicates;
    }

    @Override
    public List<Job> getListJobByMajor(int majorId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.get("companyId").get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.join("majorJobCollection").join("majorId").get("id"), majorId));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(jobRoot.get("id")));

        List<Job> jobs = session.createQuery(cq).getResultList();

        for (Job job : jobs) {
            Hibernate.initialize(job.getMajorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return jobs;
    }

    @Override
    public Job getJobById(int jobId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("id"), jobId));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.get("companyId").get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        Job job = session.createQuery(cq).uniqueResult();

        if (job != null) {
            Hibernate.initialize(job.getMajorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return job;
    }

    @Override
    public List<Job> getListJobByRecommend(int majorId, int cityId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.get("companyId").get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.join("majorJobCollection").join("majorId").get("id"), majorId));
        predicates.add(cb.equal(jobRoot.get("city"), String.valueOf(cityId)));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(jobRoot.get("id")));

        List<Job> jobs = session.createQuery(cq).getResultList();

        for (Job job : jobs) {
            Hibernate.initialize(job.getMajorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyId(int companyId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("companyId").get("id"), companyId));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.get("companyId").get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(jobRoot.get("id")));

        List<Job> jobs = session.createQuery(cq).getResultList();

        for (Job job : jobs) {
            Hibernate.initialize(job.getMajorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyId1(int companyId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("companyId").get("id"), companyId));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.get("companyId").get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(jobRoot.get("id")));

        List<Job> jobs = session.createQuery(cq).getResultList();

        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyExceptCurrentJob(int companyId, int jobId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Job getNameJob(int jobId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCandidate(int candidateId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCheckAdmin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByMajorAndCity(int majorId, String city, String kw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobForManageCompany(int companyId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateJob(int jobId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCityKw(String city, String kw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCityKwPage(String city, String kw, int page) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long countJob() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteJob(int jobId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Job job = session.get(Job.class, jobId);
            if (job != null) {
                job.setIsActive(false); // Thay vì xóa cứng, đánh dấu là không hoạt động
                session.update(job);
                logger.info("Successfully soft-deleted job with ID: " + jobId);
            } else {
                logger.warning("Job not found with ID: " + jobId);
                throw new IllegalArgumentException("Công việc không tồn tại.");
            }
        } catch (Exception e) {
            logger.severe("Error deleting job: " + e.getMessage());
            throw new RuntimeException("Lỗi khi xóa công việc: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Job> getListJobForManage() {
        Session s = this.sessionFactory.getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Job> q = cb.createQuery(Job.class);
        Root<Job> root = q.from(Job.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("isActive"), true));
        predicates.add(cb.equal(root.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(root.get("companyId").get("status"), GeneralUtils.Status.approved.toString()));

        q.where(cb.and(predicates.toArray(new Predicate[0])));
        q.orderBy(cb.asc(root.get("id")));
        return s.createQuery(q).getResultList();
    }

    @Override
    public boolean addJob(Job j) {
        Session s = this.sessionFactory.getCurrentSession();
        try {
            if (j != null) {
                s.merge(j); // Thay saveOrUpdate bằng merge
                logger.info("Successfully saved job with ID: " + (j.getId() != null ? j.getId() : "new"));
                return true;
            } else {
                logger.warning("Job object is null");
                return false;
            }
        } catch (Exception e) {
            logger.severe("Error adding job: " + e.getMessage());
            throw new RuntimeException("Lỗi khi lưu công việc: " + e.getMessage(), e);
        }
    }

    @Override
    public Job addJobDTO(Job j) { // Thay đổi để trả về Job thay vì boolean
        Session s = this.sessionFactory.getCurrentSession();
        try {
            if (j != null) {
                Job mergedJob = (Job) s.merge(j); // Gán đối tượng đã merge
                s.flush(); // Đảm bảo job được lưu và có ID
                logger.info("Successfully saved job with ID: " + (mergedJob.getId() != null ? mergedJob.getId() : "new"));
                return mergedJob;
            } else {
                logger.warning("Job object is null");
                throw new IllegalArgumentException("Công việc không hợp lệ.");
            }
        } catch (Exception e) {
            logger.severe("Error adding job: " + e.getMessage() + ", Stack trace: " + getStackTrace(e));
            throw new RuntimeException("Lỗi khi lưu công việc: " + e.getMessage(), e);
        }
    }

    @Override
    public void addDaysToJob(Job job, List<Integer> dayIds) {
        Session session = sessionFactory.getCurrentSession();
        try {
            if (job == null || job.getId() == null) {
                logger.severe("Job or Job ID is null");
                throw new IllegalArgumentException("Công việc không hợp lệ hoặc chưa được lưu.");
            }

            // Xóa các bản ghi cũ trong day_job để tránh trùng lặp
            session.createQuery("DELETE FROM DayJob dj WHERE dj.jobId.id = :jobId")
                    .setParameter("jobId", job.getId())
                    .executeUpdate();

            // Lấy danh sách ngày từ repository
            List<Day> days = session.createQuery("FROM Day", Day.class).getResultList();
            List<Integer> validDayIds = days.stream().map(Day::getId).toList();

            if (dayIds != null && !dayIds.isEmpty()) {
                for (Integer dayId : dayIds) {
                    if (dayId == null || !validDayIds.contains(dayId)) {
                        logger.warning("Invalid or null dayId: " + dayId);
                        continue;
                    }
                    Day day = days.stream().filter(d -> d.getId().equals(dayId)).findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Ngày làm việc không tồn tại: " + dayId));
                    DayJob dayJob = new DayJob();
                    dayJob.setJobId(job);
                    dayJob.setDayId(day);
                    session.save(dayJob);
                    logger.info("Added DayJob: jobId=" + job.getId() + ", dayId=" + dayId);
                }
                session.flush(); // Đảm bảo tất cả DayJob được lưu
            } else {
                logger.info("No dayIds provided for jobId: " + job.getId());
            }
        } catch (Exception e) {
            logger.severe("Error adding days to job: " + e.getMessage() + ", Stack trace: " + getStackTrace(e));
            throw new RuntimeException("Lỗi khi thêm ngày làm việc: " + e.getMessage(), e);
        }
    }

    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}