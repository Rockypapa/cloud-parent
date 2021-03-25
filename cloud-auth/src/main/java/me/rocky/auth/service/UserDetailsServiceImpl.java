package me.rocky.auth.service;
import me.rocky.auth.domain.User;
import me.rocky.auth.feign.AdminFeign;
import me.rocky.auth.feign.MemberFeign;
import me.rocky.auth.utils.RequestUtils;
import me.rocky.base.BaseController;
import me.rocky.common.result.Result;
import me.rocky.common.result.ResultCode;
import me.rocky.constants.AuthConstants;
import me.rocky.model.AuthMemberDto;
import me.rocky.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Rocky
 * @version 1.0
 * @description
 * @email inaho00@foxmail.com
 * @createDate 2021/3/2 11:11
 * @log
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberFeign memberFeign;
    @Autowired
    private AdminFeign adminFeign;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = RequestUtils.getAuthClientId();

        User user = new User();
        //根据不同的clientid调用不同的处理
        switch (clientId) {
            case AuthConstants.ADMIN_CLIENT_ID:
                // 后台用户
                Result<AuthMemberDto> userRes = adminFeign.managerLogin(username);
                if (ResultCode.USER_NOT_EXIST.getCode()==userRes.getCode()) {
                    throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMessage());
                }
                AuthMemberDto authAdminDto = userRes.getData();
                authAdminDto.setClientId(clientId);
                user = new User(authAdminDto);
                break;
            case AuthConstants.WEAPP_CLIENT_ID:
                // 小程序会员
                Result<AuthMemberDto> memberRes = memberFeign.getUserByOpenId(username);
                if (ResultCode.USER_NOT_EXIST.getCode()==memberRes.getCode()) {
                    throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMessage());
                }
                AuthMemberDto authMemberDTO = memberRes.getData();
                authMemberDTO.setClientId(clientId);
                user = new User(authMemberDTO);
                break;
            default: break;
        }
        if (!user.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!user.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return user;
    }
}
