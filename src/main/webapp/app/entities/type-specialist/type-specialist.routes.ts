import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeSpecialistComponent } from './list/type-specialist.component';
import { TypeSpecialistDetailComponent } from './detail/type-specialist-detail.component';
import { TypeSpecialistUpdateComponent } from './update/type-specialist-update.component';
import TypeSpecialistResolve from './route/type-specialist-routing-resolve.service';

const typeSpecialistRoute: Routes = [
  {
    path: '',
    component: TypeSpecialistComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeSpecialistDetailComponent,
    resolve: {
      typeSpecialist: TypeSpecialistResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeSpecialistUpdateComponent,
    resolve: {
      typeSpecialist: TypeSpecialistResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeSpecialistUpdateComponent,
    resolve: {
      typeSpecialist: TypeSpecialistResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeSpecialistRoute;
