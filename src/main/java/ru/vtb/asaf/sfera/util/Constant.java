package ru.vtb.asaf.sfera.util;

public class Constant {

    public final static String LOGIN_POST_URL = "https://sfera.inno.local/api/auth/login";
    public final static String TASKS_FILTER_GET_URL = "https://sfera.inno.local/app/tasks/api/v0.1/entities?query=area%20%3D%20%27RDS%27%20and%20%28streamExecutor%3D%27%D0%90%D0%BD%D1%82%D0%B8%D1%84%D1%80%D0%BE%D0%B4%27%20or%20assignee%20in%20%28%27VTB70172628%40corp.dev.vtb%27%2C%20%27SinyukovSO%40corp.dev.vtb%27%2C%20%27vtb70018034%40corp.dev.vtb%27%2C%20%27makarenkoef%40corp.dev.vtb%27%2C%20%27KambolinaUP%40corp.dev.vtb%27%2C%20%27VTB4070095%40corp.dev.vtb%27%29%29%20and%20status%20not%20in%20%28%27closed%27%2C%20%27done%27%2C%20%27rejectedByThePerformer%27%29&size=1000&page=0&attributesToReturn=number%2Cname%2CactualSprint%2Cpriority%2Cstatus%2Cestimation%2Cspent%2Cassignee%2Cowner%2CdueDate%2CupdateDate%2CcreateDate";
    public final static String TASKS_GET_URL = "https://sfera.inno.local/app/tasks/api/v0.1/entities";
    public final static String TASK_GET_URL = "https://sfera.inno.local/app/tasks/api/v0.1/entities/";
    public final static String HISTORY_GET_URL = "https://sfera.inno.local/app/tasks/api/v0.1/entities/{taskName}/history";
    public final static String PROJECT_CONSUMER_GET_URL = "https://sfera.inno.local/app/tasks/api/v0.1/records/{projectConsumer}";
    public final static String PARENT_CHAIN_GET_URL = "https://sfera.inno.local/app/tasks/api/v0.1/entities/{taskName}/parents-chain\n";

    public final static String HISTORY_GET_FULL_URL = "https://sfera.inno.local/app/tasks/api/v0.1/entities/RDS-152081/history?page=0&size=7&sort=createDate,desc";
}
