package com.wang.elm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.elm.entity.AddressBook;
import com.wang.elm.mapper.AddressBookMapper;
import com.wang.elm.service.AddressBookService;
import org.springframework.stereotype.Service;
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
