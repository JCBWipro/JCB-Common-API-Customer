package com.wipro.jcb.livelink.app.machines.service.impl;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.commonUtils.Utilities;
import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.Operator;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.MachineFuelConsumptionDataRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.request.MachineProfileRequest;
import com.wipro.jcb.livelink.app.machines.service.MachineProfileService;
import com.wipro.jcb.livelink.app.machines.service.MachineResponseService;
import com.wipro.jcb.livelink.app.machines.service.response.CustomerInfo;
import com.wipro.jcb.livelink.app.machines.service.response.GeofenceParam;
import com.wipro.jcb.livelink.app.machines.service.response.MachineProfile;
import com.wipro.jcb.livelink.app.machines.service.response.TimefenceParam;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@PropertySource("application.properties")
public class MachineProfileServiceImpl implements MachineProfileService {
	
	@Autowired
	Utilities utilities;
	
	@Autowired
	AppServerTokenServiceImpl appServerTokenServiceImpl;
	
//	@Autowired
//	LivelinkUserTokenServiceImpl livelinkUserTokenServiceImpl;
	
	@Autowired
	MachineRepository machineRepository;
	
	@Autowired
	ServletContext servletContext;
	
//	@Value("${cloud.aws.s3.bucket}")
//	private String bucketName;
	
//	@Value("${cloud.aws.endpoint}")
//	private String imageUrl;
	
	@Value("file:")
	private Resource res;
	
	@Autowired
	ServletContext context;
	
	@Value("${custom.formatter.timezone}")
	private String timezone;
	
//	@Autowired
//	private S3Wrapper s3Wrapper;
	
	@Autowired
	MachineFuelConsumptionDataRepository machineFuelConsumptionDataRepository;
	
	@Autowired
	MachineResponseService machineResponseService;
	
	
	@JsonIgnore
	private DateFormat df = new SimpleDateFormat(AppServerConstants.DateFormatForMachineUpdate);

	@Override
	public MachineProfile getMachineProfile(String userName, String vin) throws ProcessCustomError {
		final Machine machine = machineRepository.findByVinAndUserName(vin, userName);
		String machineType= machineFuelConsumptionDataRepository.getMachineTypeByVin(vin);
		SimpleDateFormat renewalDate = new SimpleDateFormat("dd/MM/yyyy");
		log.debug(" Processing getMachineProfile request for vin " + vin+"MachineType"+machineType);
		if (machine != null) {
			try {
				TimefenceParam timefenceParam = new TimefenceParam(machine.getStartTime(), machine.getEndTime());
				GeofenceParam geofenceParam = new GeofenceParam(machine.getCenterLat(), machine.getCenterLong(),
						machine.getRadius());
				
				CustomerInfo customerInfo = new CustomerInfo(machine.getCustomerName()!=null ? machine.getCustomerName() : "-", machine.getCustomerNumber()!=null ? machine.getCustomerNumber() : "-" , machine.getCustomerAddress()!=null ? machine.getCustomerAddress() : "-");
				
				MachineProfile machineProfile = new MachineProfile(vin, machine.getModel(), machine.getPlatform(), machine.getTag(),
						machine.getDealerName()!=null ? machine.getDealerName() : "-",machine.getDealerNumber() !=null ? machine.getDealerNumber() : "-",
						"", "","", null,null,false, machine.getImage(),machine.getThumbnail(), machine.getFirmwareVersion(), machine.getImeiNumber(),
						machine.getImsiNumber(), machine.getTransitMode(), machine.getSite(), geofenceParam,
						timefenceParam, customerInfo, machine.getRenewalDate() == null ? "" : renewalDate.format(machine.getRenewalDate()));
				machineType = (null!= machineType)? machineType:"-";
				machineProfile.setMachineType(machineType);		
				machineProfile.setFirmwareType(machineResponseService.getMachinetype(vin));
				 return machineProfile;
			} catch (final Exception ex) {
				log.error("getMachineProfile processing failed for vin " + vin);
				throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, ex.getMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			log.error("getMachineProfile: Machine not found with vin " + vin);
			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, MessagesList.APP_REQUEST_PROCESSING_FAILED,
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@Override
	public void removeMachineProfileImage(String userName, String vin) throws ProcessCustomError {
//  *************  This removeMachineProfileImage() Will be Implemented Later  **************
//		final Machine machine = machineRepository.findByVinAndUserName(vin, userName);
//		if (machine != null) {
//			//code to delete image from s3 bucket
//			if (machine.getImage() != null && machine.getImage().contains(bucketName)) {
//				s3Wrapper.delete(machine.getImage().split(bucketName + "/")[1]);
//			}
//			if (machine.getThumbnail() != null && machine.getThumbnail().contains(bucketName)) {
//				s3Wrapper.delete(machine.getThumbnail().split(bucketName + "/")[1]);
//			}
//			machine.setImage("");
//			machine.setThumbnail("");
//			machineRepository.save(machine);
//			
//		} else {
//			throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, HttpStatus.EXPECTATION_FAILED);
//		}
	}

	@Override
	public String putMachineProfile(String userName, String vin, String operatorName, String phoneNumber, String hours,
			String workStart, String workEnd, String jcbCertified, String tag, String site, MultipartFile image)
			throws ProcessCustomError {
		try {
		log.debug(" Processing putMachineProfile request for vin " + vin);
		final Operator operator = new Operator();
		try {
			operator.setOperatorName(operatorName);
			operator.setPhoneNumber(phoneNumber);
			operator.setHours(hours);
			if (workStart != null && !workStart.isEmpty()) {
				operator.setWorkStart(utilities.getDate(workStart));
			}
			if (workEnd != null && !workEnd.isEmpty()) {
				operator.setWorkEnd(utilities.getDate(workEnd));
			}
			if (jcbCertified != null) {
				operator.setJcbCertified(Boolean.parseBoolean(jcbCertified));
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.error("Unparsable data entry found for date or certification sattus");
		}
		final MachineProfileRequest machineProfileRequest = new MachineProfileRequest(vin, operator, tag, site);
		final Machine machine = machineRepository.findByVinAndUserName(machineProfileRequest.getVin(), userName);
		if (machine != null) {
			if (image != null) {
				if (image.getSize() > 0) {
					final InputStream is = image.getInputStream();
					final BufferedImage originalBufferedImage = ImageIO.read(is);
					final int thumbnailWidth = 150;
					int widthToScale, heightToScale;
					if (originalBufferedImage.getWidth() > originalBufferedImage.getHeight()) {
						heightToScale = (int) (1.1 * thumbnailWidth);
						widthToScale = (int) ((heightToScale * 1.0) / originalBufferedImage.getHeight()
								* originalBufferedImage.getWidth());
					} else {
						widthToScale = (int) (1.1 * thumbnailWidth);
						heightToScale = (int) ((widthToScale * 1.0) / originalBufferedImage.getWidth()
								* originalBufferedImage.getHeight());
					}
					final BufferedImage resizedImage = new BufferedImage(widthToScale, heightToScale,
							originalBufferedImage.getType());
					final Graphics2D g = resizedImage.createGraphics();
					g.setComposite(AlphaComposite.Src);
					g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.drawImage(originalBufferedImage, 0, 0, widthToScale, heightToScale, null);
					g.dispose();
					final int x = (resizedImage.getWidth() - thumbnailWidth) / 2;
					final int y = (resizedImage.getHeight() - thumbnailWidth) / 2;
					if (x < 0 || y < 0) {
						throw new IllegalArgumentException("Width of new thumbnail is bigger than original image");
					}
					final BufferedImage bi = resizedImage.getSubimage(x, y, thumbnailWidth, thumbnailWidth);
					InputStream compressIs = null;
					if (bi != null) {
						final ByteArrayOutputStream os = new ByteArrayOutputStream();
						ImageIO.write(bi, "jpg", os);
						compressIs = new ByteArrayInputStream(os.toByteArray());
					} else {
						compressIs = image.getInputStream();
					}
					final String fileKey = utilities.getUniqueID().replace("-", "");
//		********    This Code Will be UnCommented Once we get the AWS S3 Bucket and ImageUrl Details  ******* 
//					if (machine.getImage() != null && machine.getImage().contains(bucketName)) {
//						s3Wrapper.delete(machine.getImage().split(bucketName + "/")[1]);
//					}
//					if (machine.getThumbnail() != null && machine.getThumbnail().contains(bucketName)) {
//						s3Wrapper.delete(machine.getThumbnail().split(bucketName + "/")[1]);
//					}
//					s3Wrapper.upload(image.getInputStream(), fileKey + ".jpg");
//					s3Wrapper.upload(compressIs, fileKey + "_thumb.jpg");
//					machine.setThumbnail(imageUrl + "/" + bucketName + "/" + fileKey + "_thumb.jpg");
//					machine.setImage(imageUrl + "/" + bucketName + "/" + fileKey + ".jpg");
				} else {
					machine.setThumbnail("");
					machine.setImage("");
				}
			}
			log.debug(" Machine image saved successfully for for vin " + vin);
			machine.setTag(machineProfileRequest.getTag());
			machine.setSite(machineProfileRequest.getSite());
			machine.setOperator(machineProfileRequest.getOperator());
			machineRepository.save(machine);
			return "Machine profile updated successfully";
		}
	} catch (final Exception ex) {
		log.error(" Processing putMachineProfile request for vin " + vin + "failed with " + ex.getMessage());
		ex.printStackTrace();
		throw new ProcessCustomError("Issue while updating machineprofile for vin " + vin, ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
		log.error("Machine not found with vin " + vin);
	throw new ProcessCustomError("Invalid machine vin input", "Server can't find machine request",
			HttpStatus.EXPECTATION_FAILED);
	}
}