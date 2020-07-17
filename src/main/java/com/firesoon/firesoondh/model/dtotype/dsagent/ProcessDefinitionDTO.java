package com.firesoon.firesoondh.model.dtotype.dsagent;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 工作流定义dto
 * @author: Yz
 * @date: 2020/6/15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProcessDefinitionDTO {

    private ProcessDefinitionJson processDefinitionJson = new ProcessDefinitionJson();
    private String name;
    private String description;
    private Map<String, Location> locations = new HashMap<String, Location>() {{
        put("task-1234", new Location());
    }};
    private List<Connects> connects;
    private String projectName;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ToString
    public static class ProcessDefinitionJson {
        private List<GlobalParams> globalParams = new ArrayList<>();
        private List<Tasks> tasks = new ArrayList<Tasks>() {{
            add(new Tasks());
        }};
        private int tenantId = -1;
        private int timeout = 0;


        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @ToString
        public static class GlobalParams {
            private String prop;
            private String direct;
            private String type;
            private String value;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @ToString
        public static class Tasks {
            private String type = "SHELL";
            private String id = "task-1234";
            private String name = "spark同步任务结点";
            private Params params = new Params();
            private String description;
            private String runFlag = "NORMAL";
            private Dependence dependence = new Dependence();
            private int maxRetryTimes = 0;
            private String retryInterval = "1";
            private Timeout timeout = new Timeout();
            private String taskInstancePriority = "MEDIUM";
            private int workerGroupId = -1;
            private List<String> preTasks = new ArrayList<>();

            @AllArgsConstructor
            @NoArgsConstructor
            @Data
            @ToString
            public static class Params {
                private List<ResourceList> resourceList = new ArrayList<>();
                private List<LocalParams> localParams = new ArrayList<>();
                private String rawScript = "java -cp /opt/cq_ds/dim_hosp_comm_org/add/conf2:/opt/FireSoonDS/newFDS/FireSoonDS_2.12-1.0.0.jar:/opt/FireSoonDS/dep_lib/*: com.firesoon.DSApp";

                @AllArgsConstructor
                @NoArgsConstructor
                @Data
                @ToString
                public static class ResourceList {
                    private Integer id;
                    private String name;
                    private String res;
                }

                @AllArgsConstructor
                @NoArgsConstructor
                @Data
                @ToString
                public static class LocalParams {
                    private String prop;
                    private String direct;
                    private String type;
                    private String value;
                }
            }

            @AllArgsConstructor
            @Data
            @ToString
            public static class Dependence {
            }

            @AllArgsConstructor
            @NoArgsConstructor
            @Data
            @ToString
            public static class Timeout {
                private String strategy = "";
                private String interval;
                private boolean enable = false;
            }
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ToString
    public static class Location {
        private String name = "spark同步任务结点";
        private String targetarr;
        private int x = 579;
        private int y = 77;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ToString
    public static class Connects {
        private String endPointSourceId;
        private String endPointTargetId;
    }

}
