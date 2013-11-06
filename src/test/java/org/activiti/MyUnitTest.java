package org.activiti;

import org.activiti.engine.*;
import org.activiti.engine.impl.ManagementServiceImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUnitTest {

	@Rule
	public org.activiti.engine.test.ActivitiRule activitiRule = new ActivitiRule();

	private static Logger L = LoggerFactory.getLogger(MyUnitTest.class);
	ProcessEngine processEngine;
	RepositoryService repositoryService;
	//@Test
	@Deployment(resources = {"flowfile/my-process.bpmn20.xml"})
	public void test() {
		//ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
		//assertNotNull(processInstance);
		//
		//Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
		//assertEquals("Activiti is awesome!", task.getName());
	}

	@Test
	@Deployment(resources = {"flowfile/VacationRequest.bpmn20.xml"})
	public void testVacation() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("employeeName", "Kermit");
		params.put("numberOfDays", 4);
		params.put("vacationMotivation", "I'm really tired!");

		processEngine = activitiRule.getProcessEngine();


		repositoryService = processEngine.getRepositoryService();
		////加载流程文件
		//repositoryService.createDeployment()
		//		.addClasspathResource("flowfile/VacationRequest.bpmn20.xml")
		//		.addClasspathResource("flowfile/my-process.bpmn20.xml")
		//		.deploy();
		//
		////获取已加载流程数量
		L.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
		//

		//
		//RuntimeService runtimeService = processEngine.getRuntimeService();

		//挂起一个流程
		//suspendProcess();

		//启动一个指定的流程并传入相关参数
		//ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", params);
		//L.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());
		//completeTask(getTaskService());
		//ManagementService managementService = new ManagementServiceImpl();
		//L.info("Table name: " + managementService.getTableName(Task.class));

	}

	/**
	 * 获取可执行的任务
	 * @return	任务列表
	 */
	private List<Task> getTaskService() {
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for (Task task : tasks) {
			L.info("Task available: " + task.getName());
		}
		return tasks;
	}

	/**
	 * 完成任务
	 * @param taskList	任务列表
	 */
	private void completeTask(List<Task> taskList) {
		if (null == taskList || taskList.isEmpty()) {
			L.info("Task list is empty!");
			return;
		}
		Task task = taskList.get(0);
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		//设置当前执行的任务需要的参数
		taskVariables.put("vacationApproved", "false");
		taskVariables.put("managerMotivation", "We have a tight deadline!");
		processEngine.getTaskService().complete(task.getId(), taskVariables);
		L.info("任务完成");
	}

	/**
	 * 挂起一个流程
	 */
	private void suspendProcess() {
		//挂起一个指定流程
		repositoryService.suspendProcessDefinitionByKey("vacationRequest");
		try {
			//挂起的流程不能开始执行
			processEngine.getRuntimeService().startProcessInstanceByKey("vacationRequest");
		} catch (ActivitiException e) {
			L.error("流程启动失败", e);
		}
	}

}
