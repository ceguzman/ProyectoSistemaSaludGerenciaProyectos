import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeDocumentComponent } from './list/type-document.component';
import { TypeDocumentDetailComponent } from './detail/type-document-detail.component';
import { TypeDocumentUpdateComponent } from './update/type-document-update.component';
import TypeDocumentResolve from './route/type-document-routing-resolve.service';

const typeDocumentRoute: Routes = [
  {
    path: '',
    component: TypeDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeDocumentDetailComponent,
    resolve: {
      typeDocument: TypeDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeDocumentUpdateComponent,
    resolve: {
      typeDocument: TypeDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeDocumentUpdateComponent,
    resolve: {
      typeDocument: TypeDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeDocumentRoute;
