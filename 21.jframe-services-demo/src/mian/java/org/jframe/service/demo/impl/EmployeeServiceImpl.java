package org.jframe.service.demo.impl;

import org.jframe.core.extensions.*;
import org.jframe.core.helpers.StringHelper;
import org.jframe.data.entities.*;
import org.jframe.service.demo.EmployeeService;
import org.jframe.service.demo.dto.EmployeeDto;
import org.jframe.service.demo.dto.EmployeeEditDto;
import org.jframe.services.core.ServiceBase;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * created by yezi on 2018/1/16
 */
@Service("demo-employee-service")
public class EmployeeServiceImpl extends ServiceBase implements EmployeeService {

    @Override
    public PageResult<EmployeeDto> searchByDepartmentId(PageRequest request, Long departmentId) {
        return super.getFromDb(db -> EmployeeDto.searchByDepartmentId(request, db, departmentId)).calculateRank();
    }

    @Override
    public void save(Employee employee, String cvsRoleIds) {
        super.useTransaction(db -> {
            JList<Long> roleIds = JList.splitByComma(cvsRoleIds).where(x -> !StringHelper.isNullOrWhitespace(x)).select(x -> Long.parseLong(x));
            Role dbSuperAdmin = db.getRoleSet().getSuperAdmin();
            if (roleIds.contains(dbSuperAdmin.getId())) {
                throw new KnownException("非法操作");
            }
            if (employee.isToCreate()) {
                User user = db.getUserSet().get(employee.getUserId());
                if (!employee.getName().equals(user.getTrueName())) {
                    user.setTrueName(employee.getName());
                    db.save(user);
                }
                db.save(employee);
                for (Long roleId : roleIds) {
                    db.save(new UserRoleRL(employee.getUserId(), roleId));
                }
            } else {
                Employee dbEmployee = db.getEmployeeSet().get(employee.getId());
                if (dbEmployee == null) {
                    throw new KnownException("数据不存在");
                }
                User user = db.getUserSet().get(employee.getUserId());
                if (!employee.getName().equals(user.getTrueName())) {
                    user.setTrueName(employee.getName());
                    db.save(user);
                }
                dbEmployee.update(employee);
                db.save(dbEmployee);
                JList<Long> dbRoleIds = db.getUserRoleRLSet().getAllByUserId(dbEmployee.getUserId()).select(x -> x.getRoleId()).where(x -> !Objects.equals(x, dbSuperAdmin.getId()));
                for (Long dbRoleId : dbRoleIds) {
                    UserRoleRL dbUserRoleRL = db.getUserRoleRLSet().getByUserIdAndRoled(dbEmployee.getUserId(), dbRoleId);
                    if (!Objects.isNull(dbUserRoleRL)) {
                        db.delete(dbUserRoleRL);
                    }
                }
                for (Long roleId : roleIds) {
                    db.save(new UserRoleRL(employee.getUserId(), roleId));
                }

            }
        });
    }

    @Override
    public Employee get(Long id) {
        return super.getFromDb(db -> db.getEmployeeSet().get(id));
    }

    @Override
    public Employee getByPhone(String phone) {
        return super.getFromDb(db -> db.getEmployeeSet().getByPhone(phone));
    }

    @Override
    public JMap<String, JMap<String, JList<String>>> getRolePermissonsByUserId(Long userId) {
        return super.getFromDb(db -> {
            JList<UserRoleRL> userRoleRLs = db.getUserRoleRLSet().getAllByUserId(userId);
            JList<Long> roleIds = userRoleRLs.select(x -> x.getRoleId());
            JList<Role> roles = db.getRoleSet().getAll(roleIds);
            JMap<String, JMap<String, JList<String>>> rolePermissions = new JMap<>();
            for (Role role : roles) {
                JList<Permission> permissions = db.getPermissionSet().getPermissionsByCodes(role.getCodes());
                JMap<String, JList<String>> permissionGroup = new JMap<>();
                for (Permission permission : permissions) {
                    if (permissionGroup.containsKey(permission.getGroup())) {
                        JList<String> permissionNames = permissionGroup.get(permission.getGroup());
                        permissionNames.add(permission.getName());
                        permissionGroup.put(permission.getGroup(), permissionNames);
                        continue;
                    }
                    JList<String> permissionNames = new JList<>();
                    permissionNames.add(permission.getName());
                    permissionGroup.put(permission.getGroup(), permissionNames);
                }
                rolePermissions.put(role.getName(), permissionGroup);
            }
            return rolePermissions;
        });
    }

    @Override
    public EmployeeEditDto getById(Long id) {
        return super.getFromDb(db -> EmployeeEditDto.getByEmployeeId(db, id));
    }

    @Override
    public void delete(Long employeeId) {
        super.useTransaction(db -> {
            Employee dbEmployee = db.getEmployeeSet().get(employeeId);
            if (Objects.isNull(dbEmployee)) {
                throw new KnownException("参数错误数据不存在");
            }
            JList<UserRoleRL> userRoleRLs = db.getUserRoleRLSet().getAllByUserId(dbEmployee.getUserId());
            for (UserRoleRL userRoleRL : userRoleRLs) {
                if (!Objects.isNull(userRoleRL)) {
                    db.delete(userRoleRL);
                }
            }
            db.delete(dbEmployee);
        });
    }

    @Override
    public Integer getSuperAdminCount() {
        return super.getFromDb(db -> db.getUserRoleRLSet().getSuperAdminCount());
    }
}
