package ru.hutoroff.jagpb.bot.messages;

import com.vdurmont.emoji.EmojiManager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;
import ru.hutoroff.jagpb.data.model.Voter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PollInfoBuilder {
	private static final String BOX_UP_AND_RIGHT = String.valueOf('\u2514');
	private static final String MALE_SHRUG = EmojiManager.getForAlias("male_shrug").getUnicode();

	private static final String HEADER_FORMAT = EmojiManager.getForAlias("bar_chart").getUnicode() + " %s\n";
	private static final String OPTION_FORMAT = "\n<b>%s</b> - <i>%d</i>";
	private static final String VOTER_FORMAT = "\n\t" + BOX_UP_AND_RIGHT + EmojiManager.getForAlias("bust_in_silhouette").getUnicode() +  " %s%s";
	private static final String VOTED_N_TOTAL_FORMAT = "\n" + EmojiManager.getForAlias("clipboard").getUnicode() + " %d voted";
	private static final String VOTED_EMPTY_TOTAL_FORMAT = "\n" + EmojiManager.getForAlias("clipboard").getUnicode() + " nobody voted";

	private static final String REPORT_VOTED_TITLE_FORMAT = "Poll <b>%s</b>:\n";
	private static final String REPORT_VOTED_OPTION_FORMAT = "\n<b>%s</b> voted (%d): %s";
	private static final String REPORT_VOTED_TOTAL_FORMAT = "\n\nTotal voted: %d";

	public static SendMessage buildPollMessage(PollDO poll, Long chatId) {
		final String text = prepareText(poll);
		final SendMessage result = new SendMessage(chatId, text);

		if (poll.getOptions() != null) {
			result.setReplyMarkup(prepareKeybaord(poll.getOptions(), poll.getId().toString()));
		}

		return result;
	}

	public static InlineKeyboardMarkup prepareKeybaord(List<PollOption> options, String pollId) {
		final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(options.size());

		for (PollOption option : options) {
			InlineKeyboardButton button = new InlineKeyboardButton(option.getTitle());
			button.setCallbackData(String.format("vote_%s_%s", pollId, option.getId()));
			keyboard.add(Collections.singletonList(button));
		}
		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	public static String prepareText(PollDO poll) {
		StringBuilder sb = new StringBuilder(String.format(HEADER_FORMAT, poll.getTitle()));

		int votedCount = 0;
		if (poll.getOptions() != null) {
			for (PollOption pollOption : poll.getOptions()) {
				final Set<Voter> voters = pollOption.getVoters() != null ? pollOption.getVoters() : Collections.emptySet();
				sb.append(String.format(OPTION_FORMAT, pollOption.getTitle(), voters.size()));
				for (Voter voter : voters) {
					votedCount++;
					String name = String.format("%s %s", voter.getFirstName(), voter.getLastName());
					name = name.equals(" ") ? "" : " (" + name + ")";
					sb.append(String.format(VOTER_FORMAT, voter.getUsername(), name));
				}
			}
		}

		sb.append("\n");
		if (votedCount > 0) {
			sb.append(String.format(VOTED_N_TOTAL_FORMAT, votedCount));
		} else {
			sb.append(VOTED_EMPTY_TOTAL_FORMAT);
		}

		return sb.toString();
	}

	public static String preparePollingReport(PollDO poll) {
		if(poll.getOptions() == null || poll.getOptions().size() == 0) {
			return "No options in poll";
		}

		StringBuilder sb = new StringBuilder(String.format(REPORT_VOTED_TITLE_FORMAT, poll.getTitle()));
		int votedCounter = 0;
		for (PollOption pollOption : poll.getOptions()) {
			final Set<Voter> voters = pollOption.getVoters() != null ? pollOption.getVoters() : Collections.emptySet();
			final String votersList = voters.size() == 0 ? MALE_SHRUG : voters.stream().map(Voter::getUsername).collect(Collectors.joining(", "));
			sb.append(String.format(REPORT_VOTED_OPTION_FORMAT, pollOption.getTitle(), voters.size(), votersList));
			votedCounter += voters.size();
		}
		sb.append(String.format(REPORT_VOTED_TOTAL_FORMAT, votedCounter));

		return sb.toString();
	}
}
