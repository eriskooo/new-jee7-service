package sk.lorman.jee7.newservice.infrastructure.error;

import sk.lorman.jee7.newservice.infrastructure.domain.value.AbstractValueObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A DTO that represents an error from the Gateway.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayErrorDTO extends AbstractValueObject {

  private Integer command;

  private String errorMessage;

  private Integer eventId;

  private String processId;

  private String processName;

  private Integer responseCommand;

  private Integer systemStatus;

  private Integer userStatus;

  public Integer getCommand() {
    return command;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public Integer getEventId() {
    return eventId;
  }

  public String getProcessId() {
    return processId;
  }

  public String getProcessName() {
    return processName;
  }

  public Integer getResponseCommand() {
    return responseCommand;
  }

  public Integer getSystemStatus() {
    return systemStatus;
  }

  public Integer getUserStatus() {
    return userStatus;
  }

  @Override
  protected Object[] values() {
    return new Object[]{command, errorMessage, eventId, processId, processName, responseCommand, systemStatus, userStatus};
  }

  @Override
  public String toString() {
    return "GatewayErrorDTO{"
           + "command=" + command
           + ", errorMessage='" + errorMessage + '\''
           + ", eventId=" + eventId
           + ", processId='" + processId + '\''
           + ", processName='" + processName + '\''
           + ", responseCommand=" + responseCommand
           + ", systemStatus=" + systemStatus
           + ", userStatus=" + userStatus
           + '}';
  }
}
