<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../commons/pre_include.jsp"%>
<div class="ajax-content">
	<div class="page-header">
		<h1>
			用户管理系统 <small> <i class="ace-icon fa fa-angle-double-right"></i>用户管理
				<span class="my-button-group"> </span>
			</small> <span style="float: right">
				<button id="query" class="btn btn-info btn-sm" type="button"
					role="query">
					<i class="ace-icon fa fa-search bigger-110"></i> 查询
				</button>
				<button class="btn btn-sm" type="reset" role="reset">
					<i class="ace-icon fa fa-undo bigger-110"></i> 重置
				</button>
			</span>
		</h1>
		<!-- 隐藏域 -->
	</div>
	<div>
		<div class="row">
			<div class="col-xs-12">
				<div class="row">
					<!-- row -->
					<div class="col-xs-12">
						<form id="searchForm" class="form-horizontal">
							<div class="form-group">
								<label class="control-label col-sm-1 no-padding-right">证件类型：</label>
								<div class="col-sm-2">
									<select name="idType" id="idType"
										class="form-control col-sm-5 input-sm">
									</select>
								</div>
							</div>
							<br />
						</form>
					</div>
					<!-- end row -->
					<!-- row -->
					<div class="row">
						<div class="col-xs-12">
							<span style="float: right">
										<button id="addUser" class="btn btn-info btn-sm" type="button"
											role="addUser">
											<i class="glyphicon glyphicon-save"></i> 新增
										</button>
							</span>
							<!-- PAGE CONTENT BEGINS -->
							<div class="row">
								<div class="col-xs-12">
									<table id="tbl"
										class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th><input type="checkbox" name="selectAll"
													id="selectAll" value=""> <br></th>
												<th>用户名</th>
												<th>昵称</th>
												<th>生日</th>
												<th>email</th>
												<th>办公室电话</th>
												<th>手机</th>
												<th style="width:7%;">操作</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
					<!-- end row -->
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use('userMng/main');
</script>