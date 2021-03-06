--
-- Copyright 2010 University of California at Berkeley
-- Licensed under the Educational Community License (ECL), Version 2.0.
-- You may not use this file except in compliance with this License.
--
-- use cspace;
CREATE INDEX IF NOT EXISTS index_rolename ON roles (rolename);
CREATE INDEX IF NOT EXISTS index_rolegroup ON roles (rolegroup);
CREATE INDEX IF NOT EXISTS index_tenant_id ON roles (tenant_id);

CREATE INDEX IF NOT EXISTS index_user_id ON accounts_roles (user_id);
CREATE INDEX IF NOT EXISTS index_account_id ON accounts_roles (account_id);
CREATE INDEX IF NOT EXISTS index_acct_role_id ON accounts_roles (role_id);

CREATE INDEX IF NOT EXISTS index_permission_id ON permissions_roles (permission_id);
CREATE INDEX IF NOT EXISTS index_perm_role_id ON permissions_roles (role_id);
