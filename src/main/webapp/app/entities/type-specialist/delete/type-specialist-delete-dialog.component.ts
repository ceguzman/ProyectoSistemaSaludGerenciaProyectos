import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeSpecialist } from '../type-specialist.model';
import { TypeSpecialistService } from '../service/type-specialist.service';

@Component({
  standalone: true,
  templateUrl: './type-specialist-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeSpecialistDeleteDialogComponent {
  typeSpecialist?: ITypeSpecialist;

  protected typeSpecialistService = inject(TypeSpecialistService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeSpecialistService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
