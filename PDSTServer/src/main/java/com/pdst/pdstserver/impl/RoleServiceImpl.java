package com.pdst.pdstserver.impl;

import com.pdst.pdstserver.model.Role;
import com.pdst.pdstserver.repository.RoleRepository;
import com.pdst.pdstserver.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
