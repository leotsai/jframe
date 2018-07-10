package org.jframe.service.admin.impl;

import org.jframe.core.extensions.JList;
import org.jframe.core.exception.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.web.TreeItem;
import org.jframe.data.caching.DbCacheContext;
import org.jframe.data.entities.Permission;
import org.jframe.data.entities.Role;
import org.jframe.data.enums.DbCacheKey;
import org.jframe.service.admin.RoleSettingService;
import org.jframe.service.admin.dto.RolePermissionDto;
import org.jframe.services.core.ServiceBase;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author luoh
 * @date 2018/1/18 17:03
 */
@Service
public class RoleSettingServiceImpl extends ServiceBase implements RoleSettingService {

//    @Override
//    public void saveRole(Role role) {
//        try(WpkDbContext db = new WpkDbContext()){
//            StringHelper.validate_notNullOrWhitespace(role.getName(), "请输入角色名称");
//            db.beginTransaction();
//            if (role.isToCreate()) {
//                Role dbRole = db.getRoleSet().get(role.getName());
//                if (dbRole != null) {
//                    throw new KnownException("该角色已存在");
//                }
//                Role newRole = new Role();
//                newRole.createRole(role);
//                db.save(newRole);
//            } else {
//                Role dbRole = db.getRoleSet().get(role.getId());
//                if (dbRole == null) {
//                    throw new KnownException("数据错误，请刷新重试");
//                }
//                dbRole.updateRole(role);
//                db.save(dbRole);
//            }
//            db.commitTransaction();
//            DbCacheContext.getInstance().refreshDbVersion(DbCacheKey.ROLE);
//        }catch (Exception e){
//            if (e instanceof KnownException){
//                throw e;
//            }else {
//                throw new KnownException("网络错误，请刷新重试");
//            }
//        }
//    }

    @Override
    public void savePermission(Role role) {
        StringHelper.validate_notNullOrWhitespace(role.getName(), "请填写角色名称");
        StringHelper.validate_notNullOrWhitespace(role.getCsvCodes(), "请为该角色选择权限");
        super.useTransaction(db -> {
            if (role.isToCreate()) {
                Role dbRole = db.getRoleSet().get(role.getName());
                if (dbRole != null) {
                    throw new KnownException("该角色已存在,请修改角色名称");
                }
                Role newRole = new Role();
                newRole.createRole(role);
                db.save(newRole);
            } else {
                Role dbRole = db.getRoleSet().get(role.getId());
                if (dbRole == null) {
                    throw new KnownException("数据错误，请刷新重试");
                }
                dbRole.updateRole(role);
                db.save(dbRole);
            }
        });
        DbCacheContext.getInstance().refreshDbVersion(DbCacheKey.ROLE);
    }


    @Override
    public RolePermissionDto getPermissions(Long id) {
        return super.getFromDb(db -> {
            JList<Permission> permissions = db.getPermissionSet().getAll();
            Map<String, JList<Permission>> groupMap = permissions.toGroupedMap(x -> x.getGroup());
            JList<TreeItem> treeItems = new JList<>();
            for (Map.Entry<String, JList<Permission>> entry : groupMap.entrySet()) {
                String group = entry.getKey();
                TreeItem groupTree = new TreeItem("", group);
                JList<Permission> value = entry.getValue();
                Map<String, JList<Permission>> subGroupMap = value.toGroupedMap(y -> y.getSubGroup());
                JList<TreeItem> subtreeItems = new JList<>();
                for (Map.Entry<String, JList<Permission>> subEntry : subGroupMap.entrySet()) {
                    String subGroup = subEntry.getKey();
                    TreeItem subGroupTree = new TreeItem("", subGroup);
                    JList<Permission> values = subEntry.getValue();
                    subGroupTree.setChildren(values.select(x -> new TreeItem(x.getCode(), x.getName())));
                    subtreeItems.add(subGroupTree);
                }
                groupTree.setChildren(subtreeItems);
                treeItems.add(groupTree);
            }

            Role role = db.getRoleSet().get(id);
            if (role == null) {
                return new RolePermissionDto(new Role(),treeItems);
            }

            JList<String> codes = role.getCodes();
            for (TreeItem item : treeItems) {
                JList<TreeItem> subChildren = new JList<>();
                item.getChildren().forEach(x -> subChildren.addAll(x.getChildren()));
                subChildren.forEach(x -> {
                    if (codes.contains(x.getValue())) {
                        x.setSelected(true);
                    }
                });
                if (subChildren.all(x -> x.isSelected())) {
                    item.setSelected(true);
                }
            }

            return new RolePermissionDto(role, treeItems);

        });
    }
}
