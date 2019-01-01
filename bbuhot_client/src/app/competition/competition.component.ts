import { Component, OnInit } from '@angular/core';
import { Quiz, TeamRatio } from 'src/models';

@Component({
  selector: 'app-competition',
  templateUrl: './competition.component.html',
  styleUrls: ['./competition.component.css']
})
export class CompetitionComponent implements OnInit {
  QuizArr: Array<Quiz> = [];

  constructor() {
    this.QuizArr = [
      new Quiz(1, '����Ӯ', 2, 'δ��ʼ', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18)),
      new Quiz(2, '��һ��Ӯ', 2, 'δ��ʼ', new TeamRatio(1, 'PSG.LGD', 0.14), new TeamRatio(2, 'OasdG', 0.10)),
      new Quiz(3, '����Ӯ', 3, '�ѽ���', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18), 'left'),
      new Quiz(4, '����Ӯ', 2, 'δ��ʼ', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18)),
      new Quiz(5, '����Ӯ', 1, '������', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18)),
      new Quiz(6, '�ڶ���Ӯ', 3, '�ѽ���', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18), 'right'),
      new Quiz(7, '����Ӯ', 1, '������', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18))
    ];
  }

  onConfirmClick (inputValue: string) {
    // ���ȷ����ť��������ֵ����
    console.log(inputValue);
  }

  ngOnInit() {
  }

}
