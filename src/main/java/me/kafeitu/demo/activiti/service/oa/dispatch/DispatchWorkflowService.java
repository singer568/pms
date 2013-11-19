package me.kafeitu.demo.activiti.service.oa.dispatch;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发文会签流程Service
 * 
 * @author henryyan
 */
@Service
public class DispatchWorkflowService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  protected RuntimeService runtimeService;

  @Autowired
  protected TaskService taskService;

  @Autowired
  protected HistoryService historyService;

  @Autowired
  private IdentityService identityService;

  /**
   * 是否允许结束会签（多实例）
   * 参数的含义请参考用户手册
   */
  public Boolean canComplete(Execution execution, Integer rate, Integer nrOfInstances, Integer nrOfActiveInstances, Integer nrOfCompletedInstances,
          Integer loopCounter) {
    String agreeCounterName = "agreeCounter";
    Object agreeCounter = runtimeService.getVariable(execution.getId(), agreeCounterName);
    
    if (agreeCounter == null) {
      // 初始化计数器
      runtimeService.setVariable(execution.getId(), agreeCounterName, 1);
    } else {
      // 计数器累加
      Integer integerCounter = (Integer) runtimeService.getVariable(execution.getId(), agreeCounterName);
      runtimeService.setVariable(execution.getId(), agreeCounterName, ++integerCounter);
    }
    
    logger.debug("execution: {}" + ToStringBuilder.reflectionToString(execution));
    logger.debug("rate={}, nrOfInstances={}, nrOfActiveInstances={}, nrOfComptetedInstances={}, loopCounter={}", new Object[] { rate, nrOfInstances,
        nrOfActiveInstances, nrOfCompletedInstances, loopCounter });
    
    // 计算通过的比例，以此决定是否结束会签
    Double completeRate = new Double(nrOfCompletedInstances) / nrOfInstances;
    boolean canComlete = completeRate * 100 >= rate;
    logger.debug("rate: {}, completeRate: {}, canComlete={}", new Object[] { rate, completeRate, canComlete });
    return canComlete;
  }

}
