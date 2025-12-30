package cn.net.wenxin.quartz.util;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import cn.net.wenxin.quartz.domain.SysJob;

/**
 * 定时任务处理（禁止并发执行）
 * 
 * @author forum.wenxin.net.cn
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
